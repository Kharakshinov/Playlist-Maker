package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.SharedPreferencesWriteRead
import com.practicum.playlistmaker.search.domain.model.Track
import java.lang.reflect.Type

class SharedPreferencesWriteReadImpl (
    private val sharedPreferences: SharedPreferences
    ) : SharedPreferencesWriteRead {

    override fun readFromSharedPreferences(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList()
        val type: Type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun writeToSharedPreferences(trackList: ArrayList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    override fun saveTheme(isDark: Boolean) {
        val json = Gson().toJson(isDark)
        sharedPreferences.edit()
            .putString(THEME_KEY, json)
            .apply()
    }

    override fun getTheme(): Boolean {
        val json = sharedPreferences.getString(THEME_KEY, null) ?: return false
        val type: Type = object : TypeToken<Boolean>() {}.type
        return Gson().fromJson(json, type)
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "key_for_search_history"
        const val THEME_KEY = "key_for_theme"
    }
}