package com.practicum.playlistmaker.presentation.medialibrary.favouriteTracks

import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary


sealed class FavouriteTracksState {
    class Content(val tracks: List<TrackDomainMediaLibrary>): FavouriteTracksState()
    object Empty: FavouriteTracksState()
}