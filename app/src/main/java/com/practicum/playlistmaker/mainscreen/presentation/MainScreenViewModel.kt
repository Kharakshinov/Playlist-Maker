package com.practicum.playlistmaker.mainscreen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainScreenViewModel(): ViewModel() {

    private val _state = MutableLiveData<MainScreenState>()
    val state: LiveData<MainScreenState> = _state

    fun setStartState(){
        _state.postValue(MainScreenState.Start)
    }

    fun searchScreenOpened(){
        _state.postValue(MainScreenState.OpenSearch)
    }

    fun mediaLibraryOpened(){
        _state.postValue(MainScreenState.OpenMediaLibrary)
    }

    fun settingsOpened(){
        _state.postValue(MainScreenState.OpenSettings)
    }
}