package com.practicum.playlistmaker.presentation.medialibrary.playlists.newplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {
    fun createPlaylist(playlist: Playlist){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistsInteractor.createPlaylist(playlist)
            }
        }
    }
}