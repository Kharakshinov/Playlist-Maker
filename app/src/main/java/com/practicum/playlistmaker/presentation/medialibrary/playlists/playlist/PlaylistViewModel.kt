package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private val _state = MutableLiveData<PlaylistState>()
    val state: LiveData<PlaylistState> = _state

    private val _tracks = MutableLiveData<TracksInPlaylistState>()
    val tracks: LiveData<TracksInPlaylistState> = _tracks

    fun getPlaylistById(playlistId: Int?){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                val chosenPlaylist = playlistsInteractor.getPlaylist(playlistId)
                _state.postValue(PlaylistState.Content(chosenPlaylist))
            }
        }
    }

    fun getTracksInPlaylist(addedTracksId: ArrayList<Long>){
        lateinit var tracks: List<TrackDomainMediaLibrary>
        viewModelScope.launch{
            withContext(Dispatchers.IO){
               tracks = playlistsInteractor
                    .getTracksInPlaylist(addedTracksId)
                _tracks.postValue(TracksInPlaylistState.Content(tracks))
            }
        }
    }

}