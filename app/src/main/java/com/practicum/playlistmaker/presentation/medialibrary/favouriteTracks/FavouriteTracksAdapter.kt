package com.practicum.playlistmaker.presentation.medialibrary.favouriteTracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary

class FavouriteTracksAdapter(): RecyclerView.Adapter<FavouriteTracksViewHolder>() {
    var tracks = emptyList<TrackDomainMediaLibrary>()
    private var trackListener : onTrackClickListener? = null

    interface onTrackClickListener {
        fun onTrackClick(position: Int)
    }

    fun setOnTrackClickListener(listener: onTrackClickListener){
        trackListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteTracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return FavouriteTracksViewHolder(view, trackListener)
    }

    override fun onBindViewHolder(holder: FavouriteTracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}
