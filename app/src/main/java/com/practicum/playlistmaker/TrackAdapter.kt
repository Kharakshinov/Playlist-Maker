package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.Track

class TrackAdapter(): RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<Track>()
    private var trackListener : onTrackClickListener? = null

    interface onTrackClickListener {
        fun onTrackClick(position: Int)
    }

    fun setOnTrackClickListener(listener: onTrackClickListener){
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