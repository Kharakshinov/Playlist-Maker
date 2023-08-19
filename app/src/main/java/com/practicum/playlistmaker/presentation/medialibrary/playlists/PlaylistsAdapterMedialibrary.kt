package com.practicum.playlistmaker.presentation.medialibrary.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist

class PlaylistsAdapterMedialibrary : RecyclerView.Adapter<PlaylistsViewHolderMedialibrary>() {

    var playlists = emptyList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolderMedialibrary {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_medialibrary, parent, false)
        return PlaylistsViewHolderMedialibrary(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolderMedialibrary, position: Int) {
        holder.bind(playlists[position])
    }
}