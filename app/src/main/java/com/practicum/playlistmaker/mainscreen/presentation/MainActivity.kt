package com.practicum.playlistmaker.mainscreen.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMediaLibrary = findViewById<Button>(R.id.button_media_library)
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        val router = MainScreenRouter(this)

        val viewModel = ViewModelProvider(this, MainScreenViewModelFactory())[MainScreenViewModel::class.java]
        viewModel.state.observe(this){ state ->
            when(state){
                MainScreenState.OpenSearch -> {
                    router.openSearch()
                    viewModel.setStartState()
                }
                MainScreenState.OpenMediaLibrary -> {
                    router.openMediaPlayer()
                    viewModel.setStartState()
                }
                MainScreenState.OpenSettings -> {
                    router.openSettings()
                    viewModel.setStartState()
                }
                MainScreenState.Start -> {}
            }

        }

        buttonSearch.setOnClickListener {
            viewModel.searchScreenOpened()
        }

        buttonMediaLibrary.setOnClickListener {
            viewModel.mediaLibraryOpened()
        }

        buttonSettings.setOnClickListener {
           viewModel.settingsOpened()
        }
    }
}