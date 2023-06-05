package com.practicum.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.widget.EditText
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track

class SearchPresenter(
    private val view: SearchScreenView,
    private val interactor: SearchInteractor,
    private val router: SearchRouter
) {
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { view.loadTracks() }

    fun onClearHistoryClicked(){
        view.clearSearchHistory()
    }

    fun buttonGoBackClicked(){
        router.goBack()
    }

    fun searchTextClearClicked() {
        view.clearSearchText()
        view.hideKeyboard()
        view.hideTracks()
    }

    fun loadTracks(query: String){
        if(query.isEmpty()){
            return
        }
        view.showLoading()
        interactor.loadTracks(
            query = query,
            onSuccess = {tracks ->
                if(tracks.isEmpty()){
                    view.showEmptyResult()
                } else {
                    view.showTracks(tracks)
                }
            },
            onError = {
                view.showTracksError()
            }
        )
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun showHistoryTracksEditTextOnFocus(
        editText: EditText,
        historyTracks: ArrayList<Track>,
    ) {
        if(editText.text.isEmpty() and historyTracks.isNotEmpty() and editText.hasFocus()){
            view.showSearchHistoryViewGroup()
            view.hideNoInternetNothingFoundViews()
        } else {
            view.hideSearchHistoryViewGroup()
        }
    }

    fun onTrackClicked(position: Int) {
        if(clickDebounce()){
            val chosenTrack = view.getChosenTrack(position)
            view.showSearchHistory(position)
            router.openTrack(chosenTrack)
        }
    }

    fun onHistoryTrackClicked(position: Int) {
        if(clickDebounce()){
            val chosenTrack = view.getChosenHistoryTrack(position)
            view.addTrackOnTopSearchHistory(chosenTrack, position)
            router.openTrack(chosenTrack)
        }
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        val historyTracks = view.getTracksHistory()
        if (hasFocus and text.isEmpty() and historyTracks.isNotEmpty()){
            view.showSearchHistoryViewGroup()
        } else {
            view.hideSearchHistoryViewGroup()
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun readFromSharedPreferences(): ArrayList<Track>{
        return interactor.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<Track>){
        interactor.writeToSharedPreferences(trackList)
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}