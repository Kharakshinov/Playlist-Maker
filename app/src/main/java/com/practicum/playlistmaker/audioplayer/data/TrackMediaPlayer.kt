package com.practicum.playlistmaker.audioplayer.data

import android.media.MediaPlayer

class TrackMediaPlayer {

    private val mediaPlayer = MediaPlayer()

    fun startPlayer() {
        mediaPlayer.start()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
    }

    fun releasePlayer(){
        mediaPlayer.release()
    }

    fun setDataSource(url: String){
        mediaPlayer.setDataSource(url)
    }

    fun prepareAsync(){
        mediaPlayer.prepareAsync()
    }

    fun setOnPreparedListener(listener: (MediaPlayer) -> Unit) {
        mediaPlayer.setOnPreparedListener(listener)
    }

    fun setOnCompletionListener(listener: (MediaPlayer) -> Unit) {
        mediaPlayer.setOnCompletionListener(listener)
    }

    fun getTrackMediaPlayer(): MediaPlayer{
        return mediaPlayer
    }
}