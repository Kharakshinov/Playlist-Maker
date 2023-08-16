package com.practicum.playlistmaker.presentation.medialibrary.playlists

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist

class PlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var playlistImage: ImageView = itemView.findViewById(R.id.photoPlaylist)
    private var playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private var numberTracks: TextView = itemView.findViewById(R.id.number_tracks)

    fun bind(playlist: Playlist){
        playlistName.text = playlist.playlistName

        var trackWordEnding = R.string.track
        when (playlist.addedTracksNumber % 20) {
            1 -> {
                trackWordEnding = R.string.track
            }
            in 2..4 -> {
                trackWordEnding = R.string.track_a
            }
            0, in 5..19 -> {
                trackWordEnding = R.string.track_ov
            }
        }
        numberTracks.text = "${playlist.addedTracksNumber} $trackWordEnding"

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.dp_8)
        Glide.with(itemView)
            .load(playlist.playlistImage)
            .placeholder(R.drawable.icon_no_reply)
            .transform(RoundedCorners(radius))
            .into(playlistImage)
    }
}