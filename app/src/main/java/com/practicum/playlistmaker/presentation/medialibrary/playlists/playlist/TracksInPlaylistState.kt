package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist

import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary

sealed class TracksInPlaylistState {
    class Content(val tracks: ArrayList<TrackDomainMediaLibrary>): TracksInPlaylistState()
    object Empty: TracksInPlaylistState()
}