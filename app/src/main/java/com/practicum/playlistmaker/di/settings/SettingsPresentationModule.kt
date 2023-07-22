package com.practicum.playlistmaker.di.settings

import com.practicum.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModel{
        SettingsViewModel(get(), get())
    }
}