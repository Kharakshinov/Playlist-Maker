package com.practicum.playlistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.settings.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.ThemeInteractor

class SettingsViewModelFactory(
    private val interactor: SharingInteractor,
    private val themeInteractor: ThemeInteractor
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(interactor, themeInteractor) as T
    }
}