package com.practicum.playlistmaker.audioplayer.domain

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.data.TrackMediaPlayer

class TrackMediaPlayerInteractor {

    private val trackMediaPlayer : TrackMediaPlayer = TrackMediaPlayer()

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

    fun setOnPreparedListener(listener: (Any) -> Unit) {
        trackMediaPlayer.setOnPreparedListener(listener)
    }

    fun setOnCompletionListener(listener: (Any) -> Unit) {
        trackMediaPlayer.setOnCompletionListener(listener)
    }

    fun getMediaPlayer(): MediaPlayer {
        return trackMediaPlayer.getTrackMediaPlayer()
    }
}