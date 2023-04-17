package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var buttonGoBack: ImageView
    private lateinit var trackImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTime: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var trackTimePassed: TextView
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var chosenTrack : Track
    private lateinit var url: String
    private lateinit var handler: Handler
    private lateinit var timePassedRunnable: Runnable
    private var isPlayerUsed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)
        buttonGoBack = findViewById(R.id.button_go_back)
        trackImage = findViewById(R.id.track_image)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.track_author)
        trackTime = findViewById(R.id.track_time)
        collectionName = findViewById(R.id.track_album)
        releaseDate = findViewById(R.id.track_year)
        primaryGenreName = findViewById(R.id.track_genre)
        country = findViewById(R.id.track_country)
        playButton = findViewById(R.id.button_play_track)
        pauseButton = findViewById(R.id.button_pause_track)
        trackTimePassed  = findViewById(R.id.track_time_passed)
        handler = Handler(Looper.getMainLooper())
        val extras: Bundle? = intent.extras

        buttonGoBack.setOnClickListener {
            finish()
        }

        if (extras != null) {
            val chosenTrackJSON = extras.getString("chosen_track")
            chosenTrack = Gson().fromJson(chosenTrackJSON, Track::class.java)
        }

        val radius = resources.getDimensionPixelSize(R.dimen.dp_8)
        Glide.with(applicationContext)
            .load(chosenTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerInside()
            .placeholder(R.drawable.icon_no_reply)
            .transform(RoundedCorners(radius))
            .into(trackImage)

        trackName.text = chosenTrack.trackName
        artistName.text = chosenTrack.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(chosenTrack.trackTimeMillis)
        collectionName.text = chosenTrack.collectionName
        releaseDate.text = chosenTrack.releaseDate.take(4)
        primaryGenreName.text = chosenTrack.primaryGenreName
        country.text = chosenTrack.country
        url = chosenTrack.previewUrl

        playButton.setOnClickListener{
            playbackControl()
        }

        pauseButton.setOnClickListener{
            playbackControl()
        }

        preparePlayer()

    }

    override fun onPause() {
        pauseButton.visibility = View.GONE
        super.onPause()
        pausePlayer()
        if(isPlayerUsed){
            handler.removeCallbacks(timePassedRunnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        if(isPlayerUsed) {
            handler.removeCallbacks(timePassedRunnable)
        }
    }

    private fun createTimePassedTask(): Runnable  {
        return object : Runnable {
                override fun run() {
                    trackTimePassed.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, DELAY)
                }
            }
        }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            pauseButton.visibility = View.GONE
            playerState = STATE_PREPARED
            if(isPlayerUsed) {
                handler.removeCallbacks(timePassedRunnable)
            }
            trackTimePassed.text = getString(R.string.track_progress_0)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        if(!isPlayerUsed){
            timePassedRunnable = createTimePassedTask()
        }
        handler.post(timePassedRunnable)
        isPlayerUsed = true
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pauseButton.visibility = View.GONE
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                pauseButton.visibility = View.VISIBLE
                startPlayer()
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }
}