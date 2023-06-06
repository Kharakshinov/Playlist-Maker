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

    private val _clearHistoryLiveData = MutableLiveData<Boolean>()
    val clearHistoryLiveData: LiveData<Boolean> = _clearHistoryLiveData

    private val _showHistoryLiveData = MutableLiveData<Boolean>()
    val showHistoryLiveData: LiveData<Boolean> = _showHistoryLiveData

    private val _sharedPreferencesLiveData = MutableLiveData<ArrayList<Track>>()
    val sharedPreferencesLiveData: LiveData<ArrayList<Track>> = _sharedPreferencesLiveData

    private val _tracksLiveData = MutableLiveData<List<Track>>()
    val tracksLiveData: LiveData<List<Track>> = _tracksLiveData

    private val _searchTextClearButtonLiveData = MutableLiveData<Boolean>()
    val searchTextClearButtonLiveData: LiveData<Boolean> = _searchTextClearButtonLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private val _emptyResultLiveData = MutableLiveData<Boolean>()
    val emptyResultLiveData: LiveData<Boolean> = _emptyResultLiveData

    private val _errorLiveData = MutableLiveData<Boolean>()
    val errorLiveData: LiveData<Boolean> = _errorLiveData

    fun onClearHistoryClicked(){
        _clearHistoryLiveData.postValue(true)
    }

    fun searchTextClearClicked() {
        _searchTextClearButtonLiveData.postValue(true)
    }

    fun showHistoryTracksEditTextOnFocus(
        editText: EditText,
        historyTracks: ArrayList<Track>,
    ) {
        if(editText.text.isEmpty() and historyTracks.isNotEmpty() and editText.hasFocus()){
            _showHistoryLiveData.postValue(true)
        } else {
            _showHistoryLiveData.postValue(false)
        }
    }

    fun loadTracks(query: String){
        if(query.isEmpty()){
            return
        }
        _loadingLiveData.postValue(true)
        interactor.loadTracks(
            query = query,
            onSuccess = {tracks ->
                if(tracks.isEmpty()){
                    _emptyResultLiveData.postValue(true)
                } else {
                    _tracksLiveData.postValue(tracks)
                }
            },
            onError = {
                _errorLiveData.postValue(true)
            }
        )
    }

    fun readFromSharedPreferences(){
        val historyList = interactor.readFromSharedPreferences()
        _sharedPreferencesLiveData.postValue(historyList)
    }

    fun writeToSharedPreferences(trackList: ArrayList<Track>){
        interactor.writeToSharedPreferences(trackList)
    }

}