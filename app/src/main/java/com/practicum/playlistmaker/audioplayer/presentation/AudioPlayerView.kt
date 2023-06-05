package com.practicum.playlistmaker.audioplayer.presentation

interface AudioPlayerView {

    fun showPauseButton()
    fun hidePauseButton()
    fun goBack()
    fun playButtonAvailability(isAvailable: Boolean)
    fun updateTrackTimePassed(position: String)
}