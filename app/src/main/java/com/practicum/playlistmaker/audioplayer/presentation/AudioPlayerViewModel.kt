package com.practicum.playlistmaker.audioplayer.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.audioplayer.domain.PlayerState
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInteractor

class AudioPlayerViewModel(
    private val trackMediaPlayerInteractor: TrackMediaPlayerInteractor,
): ViewModel() {
    private var isPlayerUsed = false
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var timePassedRunnable: Runnable

    private val _state = MutableLiveData<AudioPlayerState>()
    val state: LiveData<AudioPlayerState> = _state

    init{
        _state.postValue(AudioPlayerState.NotReady)
        preparePlayer()
    }

   override fun onCleared(){
       releasePlayer()
   }

    fun onPlayButtonClicked() {
        startPlayer()
        _state.postValue(AudioPlayerState.Play(showPlayerCurrentPosition()))
    }

    fun onPauseButtonClicked() {
        pausePlayer()
        _state.postValue(AudioPlayerState.Pause)
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
        _state.postValue(AudioPlayerState.Pause)
        trackMediaPlayerInteractor.pausePlayer()
        handlerRemoveCallbacks()
    }

    private fun releasePlayer(){
        trackMediaPlayerInteractor.unSubscribeOnPlayer()
        trackMediaPlayerInteractor.releasePlayer()
        handlerRemoveCallbacks()
    }

    private fun preparePlayer() {
        trackMediaPlayerInteractor.setDataSource()
        trackMediaPlayerInteractor.prepareAsync()

        trackMediaPlayerInteractor.subscribeOnPlayer { state ->
            when(state) {
                PlayerState.NOT_READY -> {}
                PlayerState.PREPARED -> {
                    _state.postValue(AudioPlayerState.Ready)
                }
                PlayerState.COMPLETE -> {
                    _state.postValue(AudioPlayerState.OnStart)
                    handlerRemoveCallbacks()
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
                _state.postValue(AudioPlayerState.Play(showPlayerCurrentPosition()))
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
    }
}