package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun putTrackInPlaylist(track: TrackDomainAudioplayer, playlist: Playlist)
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistId: Int?)
}