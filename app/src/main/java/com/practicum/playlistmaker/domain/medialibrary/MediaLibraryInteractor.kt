package com.practicum.playlistmaker.domain.medialibrary

import com.practicum.playlistmaker.domain.search.model.TrackDomainSearch
import com.practicum.playlistmaker.domain.sharedpreferences.SharedPreferencesWriteRead

class MediaLibraryInteractor(private val sharedPreferencesWriteRead: SharedPreferencesWriteRead) {
    fun readFromSharedPreferences(): ArrayList<TrackDomainSearch>{
        return sharedPreferencesWriteRead.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<TrackDomainSearch>) {
        sharedPreferencesWriteRead.writeToSharedPreferences(trackList)
    }
}