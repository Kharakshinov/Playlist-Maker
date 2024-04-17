package com.practicum.playlistmaker.presentation.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.audioplayer.PlayerState
import com.practicum.playlistmaker.domain.audioplayer.TrackMediaPlayerInteractor
import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.db.FavouriteTracksInteractor
import com.practicum.playlistmaker.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import com.practicum.playlistmaker.presentation.medialibrary.playlists.PlaylistsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioPlayerViewModel(
    private val trackMediaPlayerInteractor: TrackMediaPlayerInteractor,
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
): ViewModel() {
    private var isPlayerUsed = false
    private var isPlayerPrepared = false
    private lateinit var favouriteTracksId: List<Long>
    private var currentAudioPlayerState: AudioPlayerState? = null

    private val _state = MutableLiveData<AudioPlayerState>()
    val state: LiveData<AudioPlayerState> = _state

    private var audioPlayerControl: AudioPlayerControl? = null

    private val _statePlaylists = MutableLiveData<PlaylistsState>()
    val statePlaylists: LiveData<PlaylistsState> = _statePlaylists

    private val _favourites = MutableLiveData<TrackState>()
    val favourites: LiveData<TrackState> = _favourites

    private val _trackToPlaylist = MutableLiveData<TrackToPlaylistState>()
    val trackToPlaylist: LiveData<TrackToPlaylistState> = _trackToPlaylist

    private var timerJob: Job? = null

    init{
        postAudioPlayerState(AudioPlayerState.NotReady)
    }

    private fun postAudioPlayerState(state: AudioPlayerState){
        _state.postValue(state)
        currentAudioPlayerState = state
    }

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect { state ->
                postAudioPlayerState(state)
            }
        }
    }

    fun startPreparingPlayer(url: String){
        if(!isPlayerPrepared)
            preparePlayer(url)
    }

   override fun onCleared(){
       releasePlayer()
       removeAudioPlayerControl()
   }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    fun onPlayButtonClicked() {
        if(currentAudioPlayerState is AudioPlayerState.Pause
            || currentAudioPlayerState is AudioPlayerState.Ready
            || currentAudioPlayerState is AudioPlayerState.OnStart) {
            audioPlayerControl?.startPlayer()
            postAudioPlayerState(AudioPlayerState.Play(showPlayerCurrentPosition()))
        } else if (currentAudioPlayerState is AudioPlayerState.Play){
            audioPlayerControl?.pausePlayer()
            postAudioPlayerState(AudioPlayerState.Pause)
        }
    }

    private fun startPlayer() {
        trackMediaPlayerInteractor.startPlayer()
        startTimer()
        isPlayerUsed = true
    }

    fun showForegroundNotification() =
        audioPlayerControl?.showForegroundNotification()

    fun hideForegroundNotification() =
        audioPlayerControl?.hideForegroundNotification()

    fun pausePlayer() {
        postAudioPlayerState(AudioPlayerState.Pause)
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
                    postAudioPlayerState(AudioPlayerState.Ready)
                }
                PlayerState.COMPLETE -> {
                    postAudioPlayerState(AudioPlayerState.OnStart)
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
                postAudioPlayerState(AudioPlayerState.Play(showPlayerCurrentPosition()))
            }
        }
    }

    fun downloadPlaylists(){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                playlistsInteractor
                    .getPlaylists()
                    .collect {
                        if(it.isEmpty())
                            _statePlaylists.postValue(PlaylistsState.Empty)
                        else
                            _statePlaylists.postValue(PlaylistsState.Content(it))
                    }
            }
        }
    }

    fun checkTrackInPlaylist(chosenTrack: TrackDomainAudioplayer, chosenPlaylist: Playlist){
        if(chosenPlaylist.addedTracksId.contains(chosenTrack.trackId))
            _trackToPlaylist.postValue(TrackToPlaylistState.InPlaylist(chosenPlaylist.playlistName))
        else
            _trackToPlaylist.postValue(TrackToPlaylistState.NotInPlaylist(chosenTrack, chosenPlaylist))
    }

    fun addTrackToPlaylist(track: TrackDomainAudioplayer, chosenPlaylist: Playlist){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                playlistsInteractor.putTrackInPlaylist(track, chosenPlaylist)
                _trackToPlaylist.postValue(TrackToPlaylistState.SuccessfulAdd(chosenPlaylist.playlistName))
            }
        }
    }

    companion object {
        private const val DELAY = 300L
    }
}