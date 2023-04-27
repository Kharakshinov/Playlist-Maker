package com.practicum.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInterface

class TrackMediaPlayer : TrackMediaPlayerInterface {

    private val mediaPlayer = MediaPlayer()

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer(){
        mediaPlayer.release()
    }

    override fun setDataSource(url: String){
        mediaPlayer.setDataSource(url)
    }

    override fun prepareAsync(){
        mediaPlayer.prepareAsync()
    }

    override fun setOnPreparedListener(listener: (MediaPlayer) -> Unit) {
        mediaPlayer.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: (MediaPlayer) -> Unit) {
        mediaPlayer.setOnCompletionListener(listener)
    }

    override fun getTrackMediaPlayer(): MediaPlayer{
        return mediaPlayer
    }
}