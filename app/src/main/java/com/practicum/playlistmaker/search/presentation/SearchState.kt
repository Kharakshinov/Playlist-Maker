package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.model.TrackDomainSearch

sealed class SearchState {
    object ClearHistory: SearchState()
    class History(val isShown: Boolean, val history: ArrayList<TrackDomainSearch>): SearchState()
    class Tracks(val tracks: List<TrackDomainSearch>): SearchState()
    object ClearTracks: SearchState()
    object Error: SearchState()
    object Empty: SearchState()
    object Loading: SearchState()
}