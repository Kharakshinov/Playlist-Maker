package com.practicum.playlistmaker.di.audioplayer

import com.practicum.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioPlayerPresentationModule = module {
    viewModel{
        AudioPlayerViewModel(get(), get(), get())
    }
}