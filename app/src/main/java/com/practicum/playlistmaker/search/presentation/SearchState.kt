package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.model.Track

sealed class SearchState {
    object ClearHistory: SearchState()
    class History(val isShown: Boolean): SearchState()
    class Tracks(val tracks: List<Track>): SearchState()
    object ClearTracks: SearchState()
    object Error: SearchState()
    object Empty: SearchState()
    object Loading: SearchState()
}