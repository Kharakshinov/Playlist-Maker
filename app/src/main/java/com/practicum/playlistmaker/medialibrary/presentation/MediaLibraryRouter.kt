package com.practicum.playlistmaker.medialibrary.presentation

import androidx.appcompat.app.AppCompatActivity

class MediaLibraryRouter(
    private val activity: AppCompatActivity
) {
    fun goBack(){
        activity.finish()
    }
}