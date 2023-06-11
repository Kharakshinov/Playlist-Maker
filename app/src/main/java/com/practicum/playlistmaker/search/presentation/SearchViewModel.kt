package com.practicum.playlistmaker.search.presentation

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track

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

    fun loadTracks(query: String){
        if(query.isEmpty()){
            return
        }
        _state.postValue(SearchState.Loading)
        interactor.loadTracks(
            query = query,
            onSuccess = {tracks ->
                if(tracks.isEmpty()){
                    _state.postValue(SearchState.Empty)
                } else {
                    _state.postValue(SearchState.Tracks(tracks))
                }
            },
            onError = {
                _state.postValue(SearchState.Error)
            }
        )
    }

    private fun readFromSharedPreferences(): ArrayList<Track>{
        return interactor.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<Track>){
        interactor.writeToSharedPreferences(trackList)
    }

}