package com.jarvis.itunesmusic.util

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.jarvis.itunesmusic.network.service.MasterService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class App : MultiDexApplication() {
    companion object {
        val instance: App = App()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun getOkHttpClient() : OkHttpClient {
        val timeout: Long = 10

        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                    CustomLog.d("OkHttp", "log: $message")
                }).setLevel(
                    HttpLoggingInterceptor.Level.BASIC
                )
            )
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build()
    }
}