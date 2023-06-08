package com.practicum.playlistmaker.audioplayer.presentation

sealed class AudioPlayerState {
    object NotReady: AudioPlayerState()
    object Ready: AudioPlayerState()
    object OnStart: AudioPlayerState()
    class Play(val currentPosition: String): AudioPlayerState()
    object Pause: AudioPlayerState()
}