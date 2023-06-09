package com.practicum.playlistmaker.medialibrary.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.practicum.playlistmaker.R

class MediaLibrary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)
        val buttonGoBack = findViewById<ImageView>(R.id.button_go_back)

        buttonGoBack.setOnClickListener {
            finish()
        }
    }
}