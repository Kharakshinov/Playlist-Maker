package com.practicum.playlistmaker.presentation.settings

sealed class SettingsState {
    class Start(val savedTheme: Boolean): SettingsState()
    object ShareApplication: SettingsState()
    object WriteToSupport: SettingsState()
    object OpenUserAgreement: SettingsState()
    class DarkTheme(val isDark: Boolean): SettingsState()
}