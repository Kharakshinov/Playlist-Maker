package com.practicum.playlistmaker.domain.sharedpreferences

import com.practicum.playlistmaker.domain.search.model.TrackDomainSearch

interface SharedPreferencesWriteRead{
    fun readFromSharedPreferences(): ArrayList<TrackDomainSearch>
    fun writeToSharedPreferences(trackList: ArrayList<TrackDomainSearch>)
    fun saveTheme(isDark: Boolean)
    fun getTheme():Boolean
}