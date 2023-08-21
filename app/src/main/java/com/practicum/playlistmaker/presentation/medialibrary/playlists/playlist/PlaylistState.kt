package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist

import com.practicum.playlistmaker.domain.medialibrary.models.Playlist

sealed class PlaylistState {
    class Content(val playlist: Playlist): PlaylistState()
}