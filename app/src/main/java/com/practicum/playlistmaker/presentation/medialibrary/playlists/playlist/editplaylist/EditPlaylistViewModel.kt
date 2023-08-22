package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist.editplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
): ViewModel() {

    private val _state = MutableLiveData<PlaylistState>()
    val state: LiveData<PlaylistState> = _state

    fun getPlaylistById(playlistId: Int?){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                val chosenPlaylist = playlistsInteractor.getPlaylist(playlistId)
                _state.postValue(PlaylistState.Content(chosenPlaylist))
            }
        }
    }

    fun editPlaylist(playlistId: Int?, playlistName: String, playlistDescription: String, playlistImage: String?){
        viewModelScope.launch{
            withContext(Dispatchers.IO) {
                playlistsInteractor.editPlaylist(
                    playlistId,
                    playlistName,
                    playlistDescription,
                    playlistImage
                )
            }
        }
    }
}