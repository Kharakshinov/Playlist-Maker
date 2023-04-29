package com.practicum.playlistmaker.search.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.audioplayer.presentation.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.model.Track

class SearchRouter(
    private val activity: AppCompatActivity
) {

    fun openTrack(chosenTrack: Track){
        val displayAudioPlayer = Intent(activity, AudioPlayerActivity::class.java)
        displayAudioPlayer.putExtra("chosen_track", Gson().toJson(chosenTrack))
        activity.startActivity(displayAudioPlayer)
    }

    fun goBack(){
        activity.finish()
    }
}