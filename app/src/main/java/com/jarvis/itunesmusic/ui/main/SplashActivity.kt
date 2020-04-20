package com.jarvis.itunesmusic.ui.main

import android.content.Intent
import android.os.Bundle
import com.jarvis.itunesmusic.ui.base.BaseActivity

class SplashActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(500)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}