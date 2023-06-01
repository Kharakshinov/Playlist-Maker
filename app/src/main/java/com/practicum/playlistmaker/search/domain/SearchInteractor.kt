package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track

class SearchInteractor (
    private val sharedPreferencesWriteRead: ISharedPreferencesWriteRead,
    private val repository: ISearchRepository
        ) {

    fun readFromSharedPreferences(): ArrayList<Track>{
        return sharedPreferencesWriteRead.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<Track>) {
        sharedPreferencesWriteRead.writeToSharedPreferences(trackList)
    }

    fun loadTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit){
        repository.loadTracks(query, onSuccess, onError)
    }
}