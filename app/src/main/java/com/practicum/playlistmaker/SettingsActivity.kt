package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonGoBack = findViewById<ImageView>(R.id.button_go_back)

        buttonGoBack.setOnClickListener {
            val displayMainMenu = Intent(this, MainActivity::class.java)
            startActivity(displayMainMenu)
        }

    }
}