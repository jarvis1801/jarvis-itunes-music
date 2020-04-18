package com.jarvis.itunesmusic.util

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.jarvis.itunesmusic.ui.custom.MusicPlayerPanel


class MusicPlayerHelper {
    companion object {
        val instance = MusicPlayerHelper()
        private var musicPlayerPanel: MusicPlayerPanel? = null
    }

    fun addMusicPlayerPanel(context: Context, rootView: ViewGroup) {
        if (musicPlayerPanel != null) {
            destroyMusicPlayer()
        }

        val musicPanelView = MusicPlayerPanel(context)

        val params: FrameLayout.LayoutParams =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.BOTTOM
        musicPanelView.layoutParams = params

        rootView.addView(musicPanelView)
        musicPlayerPanel = musicPanelView
    }

    fun destroyMusicPlayer() {
        if (musicPlayerPanel != null) {
            musicPlayerPanel!!.release()
            musicPlayerPanel = null
        }
    }
}