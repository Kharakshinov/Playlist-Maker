package com.practicum.playlistmaker.audioplayer.domain

interface TrackMediaPlayerInterface {

    var listener: TrackMediaPlayerStateListener?

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun setDataSource()

    fun prepareAsync()

    fun showCurrentPosition(): String
}