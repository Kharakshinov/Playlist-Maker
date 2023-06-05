package com.practicum.playlistmaker.audioplayer.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.data.TrackMediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInteractor
import com.practicum.playlistmaker.audioplayer.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity(), AudioPlayerView {
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
    private lateinit var chosenTrack : Track
    private lateinit var url: String
    private lateinit var audioPlayerPresenter: AudioPlayerPresenter
    private lateinit var extras: Bundle
    private val trackMediaPlayer = TrackMediaPlayer()
    private val trackMediaPlayerInteractor = TrackMediaPlayerInteractor(trackMediaPlayer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)
        initView()
        setChosenTrack()
        setTrackData()

        audioPlayerPresenter = AudioPlayerPresenter(
            view = this,
            trackMediaPlayerInteractor = trackMediaPlayerInteractor,
            url = url,
        )

        audioPlayerPresenter.preparePlayer()

        buttonGoBack.setOnClickListener {
            audioPlayerPresenter.backButtonClicked()
        }

        playButton.setOnClickListener{
            audioPlayerPresenter.onPlayButtonClicked()
        }

        pauseButton.setOnClickListener{
            audioPlayerPresenter.onPauseButtonClicked()
        }

    }

    override fun onPause() {
        super.onPause()
        hidePauseButton()
        audioPlayerPresenter.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerPresenter.releasePlayer()
    }

    private fun initView() {
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
    }

    private fun setChosenTrack(){
        extras = intent.extras!!
        val chosenTrackJSON = extras.getString(CHOSEN_TRACK)
        chosenTrack = Gson().fromJson(chosenTrackJSON, Track::class.java)
    }

    private fun setTrackData(){
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
    }

    override fun goBack() {
        finish()
    }

    override fun playButtonAvailability(isAvailable: Boolean) {
        playButton.isEnabled = isAvailable
    }

    override fun updateTrackTimePassed(position: String) {
        when(position){
            START_POSITION -> trackTimePassed.text = ZERO
            CURRENT_POSITION -> trackTimePassed.text = audioPlayerPresenter.showPlayerCurrentPosition()
        }
    }

    override fun showPauseButton() {
        pauseButton.visibility = View.VISIBLE
    }

    override fun hidePauseButton() {
        pauseButton.visibility = View.GONE
    }

    companion object {
        private const val CHOSEN_TRACK = "chosen_track"
        private const val START_POSITION = "start"
        private const val CURRENT_POSITION = "current"
        private const val ZERO = "00:00"
    }

}