package com.practicum.playlistmaker.audioplayer.presentation

interface AudioPlayerView {

    fun startPlayer()
    fun pausePlayer()
    fun showPauseButton()
    fun hidePauseButton()
    fun goBack()
}