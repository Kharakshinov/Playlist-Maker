package com.practicum.playlistmaker.audioplayer.presentation

class AudioPlayerPresenter(
    private val view: AudioPlayerView
) {

    fun onPlayButtonClicked() {
        view.startPlayer()
        view.showPauseButton()
    }

    fun onPauseButtonClicked() {
        view.pausePlayer()
        view.hidePauseButton()
    }

    fun backButtonClicked() {
        view.goBack()
    }

}