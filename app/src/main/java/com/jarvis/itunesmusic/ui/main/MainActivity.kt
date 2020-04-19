package com.jarvis.itunesmusic.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.itunesmusic.R
import com.jarvis.itunesmusic.data.model.api.Song
import com.jarvis.itunesmusic.ui.adapter.SongAdapter
import com.jarvis.itunesmusic.ui.base.BaseActivity
import com.jarvis.itunesmusic.ui.custom.SearchPanel
import com.jarvis.itunesmusic.util.MusicPlayerHelper
import com.jarvis.itunesmusic.viewmodel.SongViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), LifecycleOwner, SearchPanel.OnSearchBtnClickListener, SongAdapter.OnAdapterItemClickListener {
    private var viewModel: SongViewModel? = null
    private var songAdapter: SongAdapter? = null

    private var currentTerm = "apple"
    private var currentlimit = 25

    private var isLoading = false

    private var songListUpdateObserver: Observer<MutableList<Song>> =
        Observer { songArrayList ->
            loading.visibility = View.GONE
            if (songArrayList.size > 0) {
                songAdapter!!.songList = songArrayList
                songAdapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(this,
                    R.string.cannot_find_result, Toast.LENGTH_LONG).show()
            }
            isLoading = false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        searchPanel.setOnSearchBtnClickListener(this)
        initView()
        initViewModel()
    }

    private fun initView() {
        rList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading) {
                    val layoutManager = rList.layoutManager as LinearLayoutManager

                    val itemCount = layoutManager.itemCount
                    val lastItemPosition = layoutManager.findLastVisibleItemPosition()

                    val isNeedLoadingData: Boolean = lastItemPosition + 2 >= itemCount

                    if (isNeedLoadingData && itemCount > 0 && currentlimit < 200) {
                        currentlimit += 25
                        isLoading = true
                        getSongList(currentTerm, currentlimit.toString())
                    }
                }
            }
        })
    }


    private fun initViewModel() {
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
        getSongList(currentTerm, currentlimit.toString())
    }

    private fun getSongList(term: String, limit: String) {
        isLoading = true
        loading.visibility = View.VISIBLE
        viewModel!!.getSongList(term = term, limit = limit)
    }

    override fun onSearchClick(term: String, limit: String) {
        if (currentTerm == term)
            return

        songAdapter!!.songList.removeAll { true }
        songAdapter!!.notifyDataSetChanged()
        currentTerm = term
        currentlimit = limit.toInt()
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
