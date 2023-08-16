package com.practicum.playlistmaker.presentation.audioplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist

class PlaylistsAdapterAudioplayer : RecyclerView.Adapter<PlaylistsViewHolderAudioplayer>() {

    var playlists = emptyList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolderAudioplayer {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_audioplayer, parent, false)
        return PlaylistsViewHolderAudioplayer(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolderAudioplayer, position: Int) {
        holder.bind(playlists[position])
    }
}