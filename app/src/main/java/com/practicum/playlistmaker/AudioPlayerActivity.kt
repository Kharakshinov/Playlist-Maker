package com.practicum.playlistmaker

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)
        val buttonGoBack = findViewById<ImageView>(R.id.button_go_back)
        val trackImage: ImageView = findViewById(R.id.track_image)
        val trackName: TextView = findViewById(R.id.track_name)
        val artistName: TextView = findViewById(R.id.track_author)
        val trackTime: TextView = findViewById(R.id.track_time)
        val collectionName: TextView = findViewById(R.id.track_album)
        val releaseDate: TextView = findViewById(R.id.track_year)
        val primaryGenreName: TextView = findViewById(R.id.track_genre)
        val country: TextView = findViewById(R.id.track_country)
        val extras: Bundle? = intent.extras
        lateinit var chosenTrack : Track

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
    }
}