package com.practicum.playlistmaker.presentation.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.audioplayer.PlayerState
import com.practicum.playlistmaker.domain.audioplayer.TrackMediaPlayerInteractor
import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.db.FavouriteTracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioPlayerViewModel(
    private val trackMediaPlayerInteractor: TrackMediaPlayerInteractor,
    private val favouriteTracksInteractor: FavouriteTracksInteractor
): ViewModel() {
    private var isPlayerUsed = false
    private var isPlayerPrepared = false
    private lateinit var favouriteTracksId: List<Long>

    private val _state = MutableLiveData<AudioPlayerState>()
    val state: LiveData<AudioPlayerState> = _state

    private val _favourites = MutableLiveData<TrackState>()
    val favourites: LiveData<TrackState> = _favourites

    private var timerJob: Job? = null

    init{
        _state.postValue(AudioPlayerState.NotReady)
    }

    fun startPreparingPlayer(url: String){
        if(!isPlayerPrepared)
            preparePlayer(url)
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
        startTimer()
        isPlayerUsed = true
    }

    fun pausePlayer() {
        _state.postValue(AudioPlayerState.Pause)
        trackMediaPlayerInteractor.pausePlayer()
        timerJob?.cancel()
    }

    fun addTrackToFavourites(track: TrackDomainAudioplayer){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                favouriteTracksInteractor.putTrackInFavourites(track)
            }
        }
        _favourites.postValue(TrackState.Liked)
    }

    fun deleteTrackFromFavourites(track: TrackDomainAudioplayer){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                favouriteTracksInteractor.deleteTrackFromFavourites(track)
            }
        }
        _favourites.postValue(TrackState.NotLiked)
    }

    fun checkTrackInFavourites(track: TrackDomainAudioplayer){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                favouriteTracksInteractor
                    .getFavouriteTracksId()
                    .collect(){
                        favouriteTracksId = it
                    }
            }
            if (favouriteTracksId.contains(track.trackId))
                _favourites.postValue(TrackState.Liked)
            else
                _favourites.postValue(TrackState.NotLiked)
        }
    }

    private fun releasePlayer(){
        trackMediaPlayerInteractor.unSubscribeOnPlayer()
        trackMediaPlayerInteractor.releasePlayer()
        timerJob?.cancel()
    }

    private fun preparePlayer(url: String) {
        trackMediaPlayerInteractor.setDataSource(url)
        trackMediaPlayerInteractor.prepareAsync()
        isPlayerPrepared = true

        trackMediaPlayerInteractor.subscribeOnPlayer { state ->
            when(state) {
                PlayerState.NOT_READY -> {}
                PlayerState.PREPARED -> {
                    _state.postValue(AudioPlayerState.Ready)
                }
                PlayerState.COMPLETE -> {
                    _state.postValue(AudioPlayerState.OnStart)
                    timerJob?.cancel()
                }
            }
        }
    }

    private fun showPlayerCurrentPosition(): String{
        return trackMediaPlayerInteractor.showPlayerCurrentPosition()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (trackMediaPlayerInteractor.isPlaying()) {
                delay(DELAY)
                _state.postValue(AudioPlayerState.Play(showPlayerCurrentPosition()))
            }
        }
    }

    companion object {
        private const val DELAY = 300L
    }
}