package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track

interface SharedPreferencesWriteRead{
    fun readFromSharedPreferences(): ArrayList<Track>
    fun writeToSharedPreferences(trackList: ArrayList<Track>)
    fun saveTheme(isDark: Boolean)
    fun getTheme():Boolean
}