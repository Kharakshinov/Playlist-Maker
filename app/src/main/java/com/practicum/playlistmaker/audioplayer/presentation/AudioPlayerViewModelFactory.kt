package com.practicum.playlistmaker.audioplayer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInteractor

class AudioPlayerViewModelFactory(
    private val interactor: TrackMediaPlayerInteractor,
    private val url: String,
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(interactor, url) as T
    }
}