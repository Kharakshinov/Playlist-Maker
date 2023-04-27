package com.practicum.playlistmaker.audioplayer.presentation

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerPresenter(
    private val view: AudioPlayerView,
    private val trackMediaPlayerInteractor: TrackMediaPlayerInteractor,
    private val trackTimePassed: TextView,
    private val url: String,
    private val playButton: ImageView
) {
    private var isPlayerUsed = false
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var timePassedRunnable: Runnable


    init{
        playButton.isEnabled = false
    }

    fun onPlayButtonClicked() {
        startPlayer()
        view.showPauseButton()
    }

    fun onPauseButtonClicked() {
        pausePlayer()
        view.hidePauseButton()
    }

    fun backButtonClicked() {
        view.goBack()
    }

    private fun startPlayer() {
        trackMediaPlayerInteractor.startPlayer()
        if(!isPlayerUsed){
            timePassedRunnable = createTimePassedTask()
        }
        handler.post(timePassedRunnable)
        isPlayerUsed = true
    }

    fun pausePlayer() {
        trackMediaPlayerInteractor.pausePlayer()
        handlerRemoveCallbacks()
    }

    fun releasePlayer(){
        trackMediaPlayerInteractor.releasePlayer()
        handlerRemoveCallbacks()
    }

    fun preparePlayer() {
        trackMediaPlayerInteractor.setDataSource(url)
        trackMediaPlayerInteractor.prepareAsync()
        trackMediaPlayerInteractor.setOnPreparedListener {
            playButton.isEnabled = true
        }
        trackMediaPlayerInteractor.setOnCompletionListener {
            view.hidePauseButton()
            handlerRemoveCallbacks()
            trackTimePassed.text = START_POINT
        }
    }

    private fun createTimePassedTask(): Runnable  {
        return object : Runnable {
            override fun run() {
                trackTimePassed.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackMediaPlayerInteractor.getMediaPlayer().currentPosition)
                handler.postDelayed(this, DELAY)
            }
        }
    }

    private fun handlerRemoveCallbacks(){
        if(isPlayerUsed) {
            handler.removeCallbacks(timePassedRunnable)
        }
    }

    companion object {
        private const val DELAY = 1000L
        private const val START_POINT = "00:00"
    }

}