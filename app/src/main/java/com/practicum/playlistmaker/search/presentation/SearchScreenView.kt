package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.model.Track

interface SearchScreenView {
    fun clearSearchHistory()
    fun showSearchHistoryViewGroup()
    fun hideSearchHistoryViewGroup()
    fun showEmptyResult()
    fun showTracks(tracks: List<Track>)
    fun showTracksError()
    fun clearSearchText()
    fun hideKeyboard()
    fun hideTracks()
    fun showLoading()
    fun hideNoInternetNothingFoundViews()
    fun showSearchHistory(position: Int)
    fun addTrackOnTopSearchHistory(chosenTrack: Track, position: Int)
}