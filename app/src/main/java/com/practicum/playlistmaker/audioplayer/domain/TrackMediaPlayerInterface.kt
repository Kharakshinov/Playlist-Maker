package com.practicum.playlistmaker.audioplayer.domain

import android.media.MediaPlayer

interface TrackMediaPlayerInterface {
    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun setDataSource(url: String)

    fun prepareAsync()

    fun setOnPreparedListener(listener: (MediaPlayer) -> Unit)

    fun setOnCompletionListener(listener: (MediaPlayer) -> Unit)

    fun getTrackMediaPlayer(): MediaPlayer
}