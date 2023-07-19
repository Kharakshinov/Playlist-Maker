package com.practicum.playlistmaker.search.presentation

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.TrackDomainSearch
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: SearchInteractor,
): ViewModel() {

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    fun onClearHistoryClicked(){
        _state.postValue(SearchState.ClearHistory)
    }

    fun searchTextClearClicked() {
        _state.postValue(SearchState.ClearTracks)
    }

    fun showHistoryTracksEditTextOnFocus(
        editText: EditText,
    ) {
        if(editText.text.isEmpty() and editText.hasFocus()){
            _state.postValue(SearchState.History(true, readFromSharedPreferences()))
        } else {
            _state.postValue(SearchState.History(false, readFromSharedPreferences()))
        }
    }

    fun loadTracks(expression: String){
        if(expression.isEmpty()){
            return
        }
        _state.postValue(SearchState.Loading)

        viewModelScope.launch {
            interactor
                .loadTracks(expression)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }

    }

    private fun processResult(foundTracks: List<TrackDomainSearch>?, errorMessage: String?) {
        val tracks = mutableListOf<TrackDomainSearch>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> _state.postValue(SearchState.Error)
            tracks.isEmpty() -> _state.postValue(SearchState.Empty)
            else -> _state.postValue(SearchState.Tracks(tracks))
        }
    }

    private fun readFromSharedPreferences(): ArrayList<TrackDomainSearch>{
        return interactor.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<TrackDomainSearch>){
        interactor.writeToSharedPreferences(trackList)
    }

}