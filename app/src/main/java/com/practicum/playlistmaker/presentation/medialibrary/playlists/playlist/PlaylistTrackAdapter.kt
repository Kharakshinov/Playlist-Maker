package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary

class PlaylistTrackAdapter(): RecyclerView.Adapter<PlaylistTrackViewHolder> () {

    var tracks = listOf<TrackDomainMediaLibrary>()
    private var trackListener : OnTrackClickListener? = null

    interface OnTrackClickListener {
        fun onTrackClick(position: Int)
    }

    fun setOnTrackClickListener(listener: OnTrackClickListener){
        trackListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return PlaylistTrackViewHolder(view, trackListener)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}