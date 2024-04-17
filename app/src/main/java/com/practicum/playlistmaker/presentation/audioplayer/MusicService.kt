package com.practicum.playlistmaker.presentation.audioplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.practicum.playlistmaker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.android.ext.android.get

class MusicService : Service(), AudioPlayerControl {

    private val binder = MusicServiceBinder()

    private var mediaPlayer: MediaPlayer? = null

    private val _playerState = MutableStateFlow<AudioPlayerState>(AudioPlayerState.NotReady)
    private val playerState = _playerState.asStateFlow()

    private var songUrl = ""
    private var notificationText = ""

    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(300L)
                _playerState.value = AudioPlayerState.Play(getCurrentPlayerPosition())
            }
        }
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        mediaPlayer = get()
    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        notificationText = intent?.getStringExtra(NOTIFICATION_TEXT) ?: ""
        initMediaPlayer()
        return binder
    }

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return

        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = AudioPlayerState.Ready
        }
        mediaPlayer?.setOnCompletionListener {
            _playerState.value = AudioPlayerState.OnStart
            timerJob?.cancel()
            hideForegroundNotification()
        }
    }

    override fun showForegroundNotification(){
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(notificationText),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideForegroundNotification() {
        ServiceCompat.stopForeground(
            this,
            ServiceCompat.STOP_FOREGROUND_REMOVE
        )
    }

    private fun createServiceNotification(notificationText: String): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Playlist Maker")
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    override fun getPlayerState(): StateFlow<AudioPlayerState> = playerState

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = AudioPlayerState.Play(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = AudioPlayerState.Pause
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
            ?: "00:00"
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        releasePlayer()
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        timerJob?.cancel()
        _playerState.value = AudioPlayerState.NotReady
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private companion object {
        const val NOTIFICATION_TEXT = "notification_text"
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val SERVICE_NOTIFICATION_ID = 100
    }
}
