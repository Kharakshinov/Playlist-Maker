package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModel{
        SettingsViewModel(get(), get())
    }
}