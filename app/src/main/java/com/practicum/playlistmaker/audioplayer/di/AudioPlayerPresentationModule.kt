package com.practicum.playlistmaker.audioplayer.di

import com.practicum.playlistmaker.audioplayer.presentation.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioPlayerPresentationModule = module {
    viewModel{
        AudioPlayerViewModel(get(), get())
    }
}