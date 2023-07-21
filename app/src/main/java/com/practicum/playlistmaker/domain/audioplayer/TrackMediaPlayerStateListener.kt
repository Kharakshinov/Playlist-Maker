package com.practicum.playlistmaker.domain.audioplayer

fun interface TrackMediaPlayerStateListener {
    fun onStateChanged(state: PlayerState)
}