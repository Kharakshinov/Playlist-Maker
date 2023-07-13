package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractor (
    private val sharedPreferencesWriteRead: SharedPreferencesWriteRead,
    private val repository: SearchRepository
        ) {

    fun readFromSharedPreferences(): ArrayList<Track>{
        return sharedPreferencesWriteRead.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<Track>) {
        sharedPreferencesWriteRead.writeToSharedPreferences(trackList)
    }

    fun loadTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.loadTracks(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                else -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}