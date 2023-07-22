package com.practicum.playlistmaker.domain.audioplayer

class TrackMediaPlayerInteractor (
    private val trackMediaPlayer: TrackMediaPlayer
        ) {

    fun startPlayer(){
        trackMediaPlayer.startPlayer()
    }

    fun pausePlayer(){
        trackMediaPlayer.pausePlayer()
    }

    fun releasePlayer(){
        trackMediaPlayer.releasePlayer()
    }

    fun setDataSource(url: String){
        trackMediaPlayer.setDataSource(url)
    }

    fun prepareAsync(){
        trackMediaPlayer.prepareAsync()
    }

    fun subscribeOnPlayer(listener: TrackMediaPlayerStateListener) {
        trackMediaPlayer.listener = listener
    }

    fun unSubscribeOnPlayer() {
        trackMediaPlayer.listener = null
    }

    fun showPlayerCurrentPosition(): String{
        return trackMediaPlayer.showCurrentPosition()
    }

    fun isPlaying(): Boolean{
        return trackMediaPlayer.isPlaying()
    }
}