package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistId: Int?)
    suspend fun editPlaylist(playlistId: Int?, playlistName: String, playlistDescription: String, playlistImage: String?)
    suspend fun getPlaylist(playlistId: Int?): Playlist
    suspend fun putTrackInPlaylist(track: TrackDomainAudioplayer, playlist: Playlist)
    suspend fun deleteTrackFromPlaylist(track: TrackDomainMediaLibrary, playlist: Playlist)
    suspend fun getTracksInPlaylists(): List<TrackDomainMediaLibrary>
    suspend fun getTracksInPlaylist(addedTracksId: ArrayList<Long>): ArrayList<TrackDomainMediaLibrary>
}