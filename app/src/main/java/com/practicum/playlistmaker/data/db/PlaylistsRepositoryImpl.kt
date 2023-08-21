package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.db.entity.TrackToPlaylistEntity
import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,

): PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map { playlists -> convertFromListPlaylistEntity(playlists) }
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        val playlistEntity = convertToPlaylistEntity(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlistId: Int?) {
        appDatabase.playlistDao().deletePlaylistEntity(playlistId)
    }

    override suspend fun getPlaylist(playlistId: Int?): Playlist {
        return convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylistEntity(playlistId))
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

    override suspend fun getTracksInPlaylists(): List<TrackDomainMediaLibrary>{
        return appDatabase.trackToPlaylistDao().getTracksInPlaylists().map{ tracksInPlaylists -> convertFromTrackToPlaylistEntity(tracksInPlaylists) }
    }

    override suspend fun getTracksInPlaylist(addedTracksId: ArrayList<Long>): List<TrackDomainMediaLibrary> {
        val tracksInPlaylists = getTracksInPlaylists()
        val tracksInPlaylist = arrayListOf<TrackDomainMediaLibrary>()

        tracksInPlaylists.forEach { if(addedTracksId.contains(it.trackId)) tracksInPlaylist.add(it) }

        return tracksInPlaylist
    }

    private fun convertFromPlaylistEntity(playlistEntity: PlaylistEntity): Playlist{
        return playlistDbConverter.map(playlistEntity)
    }

    private fun convertFromListPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist>{
        return playlists.map{ playlist -> playlistDbConverter.map(playlist)}
    }

    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity{
        return playlistDbConverter.map(playlist)
    }

    private fun convertToTrackToPlaylistEntity(track: TrackDomainAudioplayer): TrackToPlaylistEntity{
        return playlistDbConverter.map(track)
    }

    private fun convertFromTrackToPlaylistEntity(trackInPlaylist: TrackToPlaylistEntity): TrackDomainMediaLibrary{
        return playlistDbConverter.map(trackInPlaylist)
    }
}