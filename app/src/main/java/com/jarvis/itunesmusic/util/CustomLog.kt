package com.jarvis.itunesmusic.util

import android.util.Log
import com.jarvis.itunesmusic.BuildConfig

class CustomLog {
    companion object {
        fun d(tag: String, msg: Any, tr: Throwable? = null) {
            if (!BuildConfig.DEBUG) {
                return
            }
            if (tr != null) Log.d(tag, msg.toString(), tr) else Log.d(tag, msg.toString())
        }
    }
}