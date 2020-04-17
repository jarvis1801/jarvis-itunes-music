package com.jarvis.itunesmusic.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.jarvis.itunesmusic.data.model.Song
import com.jarvis.itunesmusic.data.model.SongResult
import com.jarvis.itunesmusic.network.MasterRepository
import org.json.JSONObject

class SongViewModel : ViewModel() {
    private var songList: MutableLiveData<MutableList<Song>>? = null

    private var artistName: ObservableField<String> = ObservableField()
    private var trackName: ObservableField<String> = ObservableField()
    private var collectionName: ObservableField<String> = ObservableField()

    companion object {
        private const val ERROR_LIMIT = 3
    }

    init {
        songList = MutableLiveData()
    }

    fun getArtistNameAndTrackName() = "$trackName - $artistName"

    fun getCollectionName() = collectionName

    fun getSongList(term: String, limit: String) {
        var errorCount = 0
        val data: MutableLiveData<MutableList<Song>> = MutableLiveData()
        MasterRepository.instance.getSearchResult(term = term, media = "music", entity = "song", limit = limit,
            onComplete = {

            },
            onNext = {
                data.value = convertJsonToSongObjectArray(it)
            },
            onError = {
                if (++errorCount < ERROR_LIMIT) {
                    getSongList(term, limit)
                } else {
                    // todo show error
                }
            }
        )
    }

    private fun convertJsonToSongObjectArray(strings: String): MutableList<Song> {
        val originObject = Gson().fromJson(strings, SongResult::class.java)

        return originObject.results
    }

    fun disposeRepository() {
        MasterRepository.instance.dispose()
    }
}