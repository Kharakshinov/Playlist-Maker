package com.practicum.playlistmaker.audioplayer.presentation

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.audioplayer.domain.PlayerState
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInteractor

class AudioPlayerPresenter(
    private val view: AudioPlayerView,
    private val trackMediaPlayerInteractor: TrackMediaPlayerInteractor,
    private val url: String,
) {
    private var isPlayerUsed = false
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var timePassedRunnable: Runnable


    init{
        view.playButtonAvailability(false)
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
        trackMediaPlayerInteractor.unSubscribeOnPlayer()
        trackMediaPlayerInteractor.releasePlayer()
        handlerRemoveCallbacks()
    }

    fun preparePlayer() {
        trackMediaPlayerInteractor.setDataSource(url)
        trackMediaPlayerInteractor.prepareAsync()

        trackMediaPlayerInteractor.subscribeOnPlayer { state ->
            when(state) {
                PlayerState.NOT_READY -> {}
                PlayerState.PREPARED -> view.playButtonAvailability(true)
                PlayerState.COMPLETE -> {
                    view.hidePauseButton()
                    handlerRemoveCallbacks()
                    view.updateTrackTimePassed(START_POSITION)
                }
            }
        }
    }

    fun showPlayerCurrentPosition(): String{
        return trackMediaPlayerInteractor.showPlayerCurrentPosition()
    }

    private fun createTimePassedTask(): Runnable  {
        return object : Runnable {
            override fun run() {
                view.updateTrackTimePassed(CURRENT_POSITION)
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
        private const val START_POSITION = "start"
        private const val CURRENT_POSITION = "current"
    }

}