package com.practicum.playlistmaker.settings.presentation

sealed class SettingsState {
    object Start: SettingsState()
    object ShareApplication: SettingsState()
    object WriteToSupport: SettingsState()
    object OpenUserAgreement: SettingsState()
    class DarkTheme(val isDark: Boolean): SettingsState()
}