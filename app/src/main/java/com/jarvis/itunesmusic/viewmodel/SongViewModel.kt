package com.jarvis.itunesmusic.viewmodel

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.jarvis.itunesmusic.data.model.api.Song
import com.jarvis.itunesmusic.data.model.api.SongResult
import com.jarvis.itunesmusic.di.component.DaggerSongViewModelComponent
import com.jarvis.itunesmusic.network.MasterRepository
import javax.inject.Inject

class SongViewModel : ViewModel() {
    private var songList: MutableLiveData<MutableList<Song>> = MutableLiveData()
    private var errorCount = 0

    @Inject
    lateinit var masterRepository: MasterRepository

    companion object {
        private const val ERROR_LIMIT = 3
    }

    init {
        DaggerSongViewModelComponent.create().inject(this)
    }

    fun getSongList(term: String, limit: String) {
        masterRepository.getSearchResult(term = term, media = "music", entity = "song", limit = limit,
            onComplete = {

            },
            onNext = {
                songList.value = convertJsonToSongObjectArray(it)
            },
            onError = {
                if (++errorCount < ERROR_LIMIT) {
                    Handler().postDelayed({
                        getSongList(term, limit)
                    }, 1000)
                } else {
                    errorCount = 0
                    songList.value = mutableListOf()
                }
            }
        )
    }

    fun getSongMutableLiveData(): MutableLiveData<MutableList<Song>> {
        return songList
    }

    private fun convertJsonToSongObjectArray(strings: String): MutableList<Song> {
        val originObject = Gson().fromJson(strings, SongResult::class.java)

        return originObject.results
    }

    fun disposeRepository() {
        masterRepository.dispose()
    }
}