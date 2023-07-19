package com.practicum.playlistmaker.sharedpreferences.domain

import com.practicum.playlistmaker.search.domain.model.TrackDomainSearch

interface SharedPreferencesWriteRead{
    fun readFromSharedPreferences(): ArrayList<TrackDomainSearch>
    fun writeToSharedPreferences(trackList: ArrayList<TrackDomainSearch>)
    fun saveTheme(isDark: Boolean)
    fun getTheme():Boolean
}