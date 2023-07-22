package com.practicum.playlistmaker.domain.audioplayer

interface TrackMediaPlayer {

    var listener: TrackMediaPlayerStateListener?

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun setDataSource(url: String)

    fun prepareAsync()

    fun showCurrentPosition(): String

    fun isPlaying():Boolean
}