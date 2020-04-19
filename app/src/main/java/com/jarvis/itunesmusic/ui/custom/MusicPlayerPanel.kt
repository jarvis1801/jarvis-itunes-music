package com.jarvis.itunesmusic.ui.custom

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jarvis.itunesmusic.R
import com.jarvis.itunesmusic.data.model.api.Song
import com.jarvis.itunesmusic.util.MusicPlayerHelper
import kotlinx.android.synthetic.main.custom_music_player_panel.view.*
import java.text.DecimalFormat


class MusicPlayerPanel @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, song: Song? = null)
    : FrameLayout(context, attrs, defStyleAttr), Player.EventListener {

    private var player :SimpleExoPlayer? = null
    private var playbackPosition = 0L
    private var playerState = PLAYER_STATE_PLAYING
    private var mSong: Song? = null

    private var mHandlerThread: Handler? = null
    private var mRunnable: Runnable? = null
    private var isStopHandler = false

    companion object {
        private const val PLAYER_STATE_PLAYING = 0
        private const val PLAYER_STATE_PAUSE = 1
    }

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_music_player_panel, this, true)

        mSong = song

        initView()
        initPlayer(song!!)
    }

    private fun initView() {
        btnPlayPause.setOnClickListener {
            if (player != null) {
                if (playerState == PLAYER_STATE_PLAYING) {
                    pausePlayer()
                } else {
                    startPlayer()
                }
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (player != null) {
                    val totalTime = player!!.duration
                    player!!.seekTo(seekBar.progress * totalTime / 100)
                }
            }
        })

        btnClose.setOnClickListener {
            MusicPlayerHelper.instance.destroyMusicPlayer()
        }
    }

    private fun initHandler() {
        if (mRunnable == null) {
            mRunnable = Runnable {
                if (playerState == PLAYER_STATE_PLAYING && player != null) {
                    val currentTime = player!!.currentPosition
                    val totalTime = player!!.duration
                    textCurrentTime.text = formatSongTime(currentTime)
                    if (totalTime.toInt() > 0 && currentTime.toInt() > 0) {
                        seekBar.progress =
                            (currentTime.toFloat() / totalTime.toFloat() * 100).toInt()
                    }
                }
                if (!isStopHandler) {
                    postDelayed(mRunnable, 200)
                }
            }
        }

        if (mHandlerThread == null) {
            mHandlerThread = Handler(Looper.getMainLooper())
        }
        mHandlerThread!!.post(mRunnable!!)
    }

    fun initPlayer(song: Song, isReset: Boolean = false) {
        player = SimpleExoPlayer.Builder(context).build()
        player!!.addListener(this)

        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            context, Util.getUserAgent(context, song.previewUrl)
        )

        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(song.previewUrl))

        player!!.prepare(videoSource)
        player!!.seekTo(playbackPosition)
        if (!isReset) {
            startPlayer()
        }
    }

    private fun pausePlayer() {
        player!!.playWhenReady = false
        player!!.playbackState
    }

    private fun startPlayer() {
        isStopHandler = false
        initHandler()
        player!!.playWhenReady = true
        player!!.playbackState
    }

    private fun loadImage(imageId: Int) {
        Glide.with(context)
            .load(imageId)
            .into(btnPlayPause)
    }

    fun release() {
        playbackPosition = player!!.currentPosition
        player!!.release()
        isStopHandler = true
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)

        playerState = if (isPlaying) {
            loadImage(R.drawable.baseline_pause_black_48)
            PLAYER_STATE_PLAYING
        } else {
            loadImage(R.drawable.baseline_play_arrow_black_48)
            PLAYER_STATE_PAUSE
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_READY -> textTotalTime.text = formatSongTime(player!!.duration)
            ExoPlayer.STATE_ENDED -> {
                textCurrentTime.text = formatSongTime(player!!.duration)
                playbackPosition = 0L
                if (mSong != null) {
                    initPlayer(mSong!!, true)
                }
            }
            ExoPlayer.STATE_BUFFERING, ExoPlayer.STATE_IDLE -> {}
        }
    }

    private fun formatSongTime(duration: Long): String {

        val decimalFormat = DecimalFormat("00")

        var second = (duration / 1000).toInt()
        if (second > 60) {
            val minute = (second / 60)
            second %= 60
            return decimalFormat.format(minute) + ":" + decimalFormat.format(second)
        }
        return "00:" + decimalFormat.format(second)
    }
}