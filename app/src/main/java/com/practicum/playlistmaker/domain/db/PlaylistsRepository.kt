package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
}