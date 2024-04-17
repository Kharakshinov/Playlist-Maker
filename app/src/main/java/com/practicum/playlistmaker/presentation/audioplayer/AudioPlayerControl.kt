package com.practicum.playlistmaker.presentation.audioplayer

import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<AudioPlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun showForegroundNotification()
    fun hideForegroundNotification()
}