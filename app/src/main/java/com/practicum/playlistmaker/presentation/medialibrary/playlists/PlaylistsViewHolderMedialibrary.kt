package com.practicum.playlistmaker.presentation.medialibrary.playlists

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist

class PlaylistsViewHolderMedialibrary(itemView: View, listener: PlaylistsAdapterMedialibrary.OnTrackClickListener?): RecyclerView.ViewHolder(itemView) {

    private var playlistImage: ImageView = itemView.findViewById(R.id.photoPlaylist)
    private var playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private var numberTracks: TextView = itemView.findViewById(R.id.number_tracks)

    init{
        itemView.setOnClickListener {
            listener?.onTrackClick(adapterPosition)
        }
    }

    fun bind(playlist: Playlist){
        playlistName.text = playlist.playlistName

        val trackWordEnding = itemView.resources.getQuantityString(R.plurals.plurals_1, playlist.addedTracksNumber, playlist.addedTracksNumber);

        numberTracks.text = trackWordEnding

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.dp_8)
        Glide.with(itemView)
            .load(playlist.playlistImage)
            .placeholder(R.drawable.icon_no_reply)
            .transform(RoundedCorners(radius))
            .into(playlistImage)
    }
}