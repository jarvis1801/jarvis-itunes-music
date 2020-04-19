package com.jarvis.itunesmusic.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import javax.inject.Inject


class HandlerThread @Inject constructor(runnable: Runnable) {
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null

    init {
        mRunnable = runnable
    }

    fun start() {
        Log.d("chris", "1234")
        Log.d("chris", mRunnable.toString())
        mHandler = Handler()
        mHandler!!.post { mRunnable }
    }

    fun stop() {
        mHandler!!.removeCallbacks(mRunnable!!)
        mHandler = null
    }
}