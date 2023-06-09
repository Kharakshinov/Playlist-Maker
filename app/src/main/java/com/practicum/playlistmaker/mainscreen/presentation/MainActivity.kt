package com.practicum.playlistmaker.mainscreen.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R

class MainActivity : AppCompatActivity() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

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
            if(clickDebounce()){
                viewModel.searchScreenOpened()
            }
        }

        buttonMediaLibrary.setOnClickListener {
            if(clickDebounce()) {
                viewModel.mediaLibraryOpened()
            }
        }

        buttonSettings.setOnClickListener {
            if(clickDebounce()) {
                viewModel.settingsOpened()
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}