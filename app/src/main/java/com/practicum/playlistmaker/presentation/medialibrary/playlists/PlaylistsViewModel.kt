package com.practicum.playlistmaker.presentation.medialibrary.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private val _state = MutableLiveData<PlaylistsState>()
    val state: LiveData<PlaylistsState> = _state

    fun downloadPlaylists(){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                playlistsInteractor
                    .getPlaylists()
                    .collect {
                        if(it.isEmpty())
                            _state.postValue(PlaylistsState.Empty)
                        else
                            _state.postValue(PlaylistsState.Content(it))
                    }
            }
        }
    }
}