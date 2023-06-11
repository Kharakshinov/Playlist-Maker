package com.practicum.playlistmaker.audioplayer.domain

fun interface TrackMediaPlayerStateListener {
    fun onStateChanged(state: PlayerState)
}