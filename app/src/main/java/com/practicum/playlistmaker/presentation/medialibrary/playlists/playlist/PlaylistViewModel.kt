package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.domain.medialibrary.MediaLibraryInteractor
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import com.practicum.playlistmaker.domain.search.model.TrackDomainSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val mediaLibraryInteractor: MediaLibraryInteractor,
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
        lateinit var tracks: ArrayList<TrackDomainMediaLibrary>
        viewModelScope.launch{
            withContext(Dispatchers.IO){
               tracks = playlistsInteractor
                    .getTracksInPlaylist(addedTracksId)
                if(tracks.isEmpty())
                    _tracks.postValue(TracksInPlaylistState.Empty)
                else
                    _tracks.postValue(TracksInPlaylistState.Content(tracks))
            }
        }
    }

    fun deleteTrackFromPlaylist(chosenTrack: TrackDomainMediaLibrary, playlist: Playlist){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                playlistsInteractor.deleteTrackFromPlaylist(chosenTrack, playlist)
            }
        }
    }

    fun deletePlaylist(playlistId: Int?){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                playlistsInteractor.deletePlaylist(playlistId)
            }
        }
    }

    fun readFromSharedPreferences(): ArrayList<TrackDomainSearch>{
        return mediaLibraryInteractor.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<TrackDomainSearch>){
        mediaLibraryInteractor.writeToSharedPreferences(trackList)
    }
}