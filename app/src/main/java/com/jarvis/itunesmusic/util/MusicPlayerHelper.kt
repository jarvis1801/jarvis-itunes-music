package com.jarvis.itunesmusic.util

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.jarvis.itunesmusic.data.model.api.Song
import com.jarvis.itunesmusic.ui.custom.MusicPlayerPanel


class MusicPlayerHelper {
    private var mSong: Song? = null
    private var mRootView: ConstraintLayout? = null

    companion object {
        val instance = MusicPlayerHelper()
        private var musicPlayerPanel: MusicPlayerPanel? = null
    }

    fun addMusicPlayerPanel(context: Context, rootView: ConstraintLayout, song: Song) {
        mSong = song
        mRootView = rootView

        if (musicPlayerPanel != null) {
            destroyMusicPlayer()
        }

        val musicPanelView = MusicPlayerPanel(context = context, song = song)

        musicPanelView.id = View.generateViewId()

        rootView.addView(musicPanelView)

        musicPanelView.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0)

        val constraintSet = ConstraintSet()
        constraintSet.clone(rootView)
        constraintSet.connect(musicPanelView.id, ConstraintSet.BOTTOM, rootView.id, ConstraintSet.BOTTOM, 0)
        constraintSet.connect(musicPanelView.id, ConstraintSet.LEFT, rootView.id, ConstraintSet.LEFT, 0)
        constraintSet.connect(musicPanelView.id, ConstraintSet.RIGHT, rootView.id, ConstraintSet.RIGHT, 0)

        constraintSet.applyTo(rootView)

        musicPlayerPanel = musicPanelView
    }

    fun destroyMusicPlayer() {
        if (musicPlayerPanel != null && mRootView != null) {
            mRootView!!.removeView(musicPlayerPanel)
            musicPlayerPanel!!.release()

            musicPlayerPanel = null
        }
    }

    fun releaseMusicPlayer() {
        if (musicPlayerPanel != null) {
            musicPlayerPanel!!.release()
        }
    }

    fun initMusicPlayer() {
        if (musicPlayerPanel != null && mSong != null) {
            musicPlayerPanel!!.initPlayer(mSong!!)
        }
    }
}