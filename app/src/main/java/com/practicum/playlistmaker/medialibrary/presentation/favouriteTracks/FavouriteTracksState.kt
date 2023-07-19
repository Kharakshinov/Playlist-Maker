package com.practicum.playlistmaker.medialibrary.presentation.favouriteTracks

import com.practicum.playlistmaker.medialibrary.domain.models.TrackDomainMediaLibrary


sealed class FavouriteTracksState {
    class Content(val tracks: List<TrackDomainMediaLibrary>): FavouriteTracksState()
    object Empty: FavouriteTracksState()
}