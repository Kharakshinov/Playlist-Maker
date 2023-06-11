package com.practicum.playlistmaker.mainscreen.presentation

sealed class MainScreenState {
    object Start: MainScreenState()
    object OpenSearch: MainScreenState()
    object OpenMediaLibrary: MainScreenState()
    object OpenSettings: MainScreenState()
}