package com.practicum.playlistmaker.presentation.medialibrary.playlists

import com.practicum.playlistmaker.domain.medialibrary.models.Playlist

sealed class PlaylistsState {
    class Content(val playlists: List<Playlist>): PlaylistsState()
    object Empty: PlaylistsState()
}