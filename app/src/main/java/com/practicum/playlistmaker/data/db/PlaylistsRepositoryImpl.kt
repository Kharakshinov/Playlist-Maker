package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.db.entity.TrackToPlaylistEntity
import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,

): PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map { playlists -> convertFromPlaylistEntity(playlists) }
    }

    override suspend fun putTrackInPlaylist(track: TrackDomainAudioplayer, playlist: Playlist){
        val trackToPlaylistEntity = convertToTrackToPlaylistEntity(track)

        playlist.addedTracksId.add(track.trackId)
        playlist.addedTracksNumber++
        val newTracksId = playlist.addedTracksId.toString()
        val addedTracksNumber = playlist.addedTracksNumber
        val playlistId = playlist.playlistId

        appDatabase.playlistDao().changeTracksList(newTracksId, playlistId!!, addedTracksNumber)
        appDatabase.trackToPlaylistDao().insertTrack(trackToPlaylistEntity)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        val playlistEntity = convertToPlaylistEntity(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlistId: Int?) {
        appDatabase.playlistDao().deletePlaylistEntity(playlistId)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist>{
        return playlists.map{ playlist -> playlistDbConverter.map(playlist)}
    }

    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity{
        return playlistDbConverter.map(playlist)
    }

    private fun convertToTrackToPlaylistEntity(track: TrackDomainAudioplayer): TrackToPlaylistEntity{
        return playlistDbConverter.map(track)
    }
}