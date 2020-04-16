package com.jarvis.itunesmusic.network.service

import com.jarvis.itunesmusic.util.App
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MasterService {
    @GET("/search")
    fun getSearchResult(
        @Query("term") term: String,
        @Query("media") media: String,
        @Query("entity") entity: String,
        @Query("limit") limit: String
    ): Observable<String>

    companion object Factory {
        fun retrofitService(): MasterService = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(App.instance.getOkHttpClient())
            .build()
            .create(MasterService::class.java)
    }
}