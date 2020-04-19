package com.jarvis.itunesmusic

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarvis.itunesmusic.data.model.api.Song
import com.jarvis.itunesmusic.ui.adapter.SongAdapter
import com.jarvis.itunesmusic.ui.custom.SearchPanel
import com.jarvis.itunesmusic.util.MusicPlayerHelper
import com.jarvis.itunesmusic.viewmodel.SongViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LifecycleOwner, SearchPanel.OnSearchBtnClickListener, SongAdapter.OnAdapterItemClickListener {
    private var viewModel: SongViewModel? = null
    private var songAdapter: SongAdapter? = null

    private var songListUpdateObserver: Observer<MutableList<Song>> =
        Observer { songArrayList ->
            loading.visibility = View.GONE
            if (songArrayList.size > 0) {
                songAdapter!!.songList = songArrayList
                songAdapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(this, R.string.cannot_find_result, Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        searchPanel.setOnSearchBtnClickListener(this)
        setupViewModel()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)
            .get(SongViewModel::class.java)

        observeViewModel()
    }

    private fun observeViewModel() {
        songAdapter = SongAdapter(mutableListOf(), this)
        rList.layoutManager = LinearLayoutManager(this)
        rList.adapter = songAdapter

        viewModel!!.getSongMutableLiveData().observe(this, songListUpdateObserver)

        // default fetch
        getSongList("apple", "25")
    }

    private fun getSongList(term: String, limit: String) {
        loading.visibility = View.VISIBLE
        viewModel!!.getSongList(term = term, limit = limit)
    }

    override fun onSearchClick(term: String, limit: String) {
        songAdapter!!.songList.removeAll { true }
        songAdapter!!.notifyDataSetChanged()
        getSongList(term = term, limit = limit)
    }

    override fun onAdapterItemClick(song: Song) {
        MusicPlayerHelper.instance.addMusicPlayerPanel(this, rootView, song)
    }

    override fun onResume() {
        super.onResume()

        MusicPlayerHelper.instance.initMusicPlayer()
    }

    override fun onPause() {
        super.onPause()

        MusicPlayerHelper.instance.releaseMusicPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel?.disposeRepository()
        MusicPlayerHelper.instance.destroyMusicPlayer()
    }
}
