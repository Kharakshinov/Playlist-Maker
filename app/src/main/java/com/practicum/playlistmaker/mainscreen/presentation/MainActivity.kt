package com.practicum.playlistmaker.mainscreen.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMediaLibrary = findViewById<Button>(R.id.button_media_library)
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        val router = MainScreenRouter(this)

        buttonSearch.setOnClickListener {
            router.openSearch()
        }

        buttonMediaLibrary.setOnClickListener {
            router.openMediaPlayer()
        }

        buttonSettings.setOnClickListener {
           router.openSettings()
        }
    }
}