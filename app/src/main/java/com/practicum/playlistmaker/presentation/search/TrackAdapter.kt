package com.practicum.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.search.model.TrackDomainSearch

class TrackAdapter(): RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<TrackDomainSearch>()
    private var trackListener : OnTrackClickListener? = null

    interface OnTrackClickListener {
        fun onTrackClick(position: Int)
    }

    fun setOnTrackClickListener(listener: OnTrackClickListener){
        trackListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view, trackListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}