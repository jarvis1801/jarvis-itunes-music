package com.jarvis.itunesmusic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jarvis.itunesmusic.viewmodel.SongViewModel


class MainActivity : AppCompatActivity() {
    var viewModel: SongViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)
            .get(SongViewModel::class.java)


    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel?.disposeRepository()
    }
}
