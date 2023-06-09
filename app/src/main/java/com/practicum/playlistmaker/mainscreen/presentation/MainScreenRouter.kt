package com.practicum.playlistmaker.mainscreen.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.medialibrary.MediaLibrary
import com.practicum.playlistmaker.search.presentation.SearchActivity
import com.practicum.playlistmaker.settings.presentation.SettingsActivity

class MainScreenRouter(
    private val activity: AppCompatActivity
) {

    fun openSearch(){
        val displaySearch = Intent(activity, SearchActivity::class.java)
        activity.startActivity(displaySearch)
    }

    fun openMediaPlayer(){
        val displayMediaLibrary = Intent(activity, MediaLibrary::class.java)
        activity.startActivity(displayMediaLibrary)
    }

    fun openSettings(){
        val displaySettings = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(displaySettings)
    }
}