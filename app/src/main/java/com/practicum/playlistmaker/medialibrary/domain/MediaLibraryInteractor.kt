package com.practicum.playlistmaker.medialibrary.domain

import com.practicum.playlistmaker.search.domain.model.TrackDomainSearch
import com.practicum.playlistmaker.sharedpreferences.domain.SharedPreferencesWriteRead

class MediaLibraryInteractor(private val sharedPreferencesWriteRead: SharedPreferencesWriteRead) {
    fun readFromSharedPreferences(): ArrayList<TrackDomainSearch>{
        return sharedPreferencesWriteRead.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<TrackDomainSearch>) {
        sharedPreferencesWriteRead.writeToSharedPreferences(trackList)
    }
}