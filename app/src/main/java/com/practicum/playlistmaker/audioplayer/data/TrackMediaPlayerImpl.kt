package com.practicum.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.PlayerState
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerStateListener
import java.text.SimpleDateFormat
import java.util.Locale

class TrackMediaPlayerImpl(private val mediaPlayer: MediaPlayer) : TrackMediaPlayer {

    override var listener: TrackMediaPlayerStateListener? = null

    init{
        listener?.onStateChanged(PlayerState.NOT_READY)
        mediaPlayer.setOnPreparedListener{
            listener?.onStateChanged(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener{
            listener?.onStateChanged(PlayerState.COMPLETE)
        }

    }

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

    override fun showCurrentPosition(): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}