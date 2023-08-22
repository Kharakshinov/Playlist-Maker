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
import java.util.Date

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,

): PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map { playlists -> convertFromListPlaylistEntityToListPlaylist(playlists) }
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        val playlistEntity = convertFromPlaylistToPlaylistEntity(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlistId: Int?) {
        appDatabase.playlistDao().deletePlaylistEntity(playlistId)

        val tracksInPlaylists = getTracksInPlaylists()
        getPlaylists().collect{
            val playlists = it
            tracksInPlaylists.forEach { checkTrackInPlaylists(it, playlists) }
        }
    }

    override suspend fun editPlaylist(
        playlistId: Int?,
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
        appDatabase.playlistDao().editPlaylist(playlistId, playlistName, playlistDescription, playlistImage)
    }

    override suspend fun getPlaylist(playlistId: Int?): Playlist {
        return convertFromPlaylistEntityToPlaylist(appDatabase.playlistDao().getPlaylistEntity(playlistId))
    }

    override suspend fun putTrackInPlaylist(track: TrackDomainAudioplayer, playlist: Playlist){
        val trackToPlaylistEntity = convertFromTrackDomainAudioplayerToTrackToPlaylistEntity(track)
        trackToPlaylistEntity.timeSaved = Date().time

        playlist.addedTracksId.add(track.trackId)
        playlist.addedTracksNumber++
        val newTracksId = playlist.addedTracksId.toString()
        val addedTracksNumber = playlist.addedTracksNumber
        val playlistId = playlist.playlistId

        appDatabase.playlistDao().changeTracksList(newTracksId, playlistId!!, addedTracksNumber)
        appDatabase.trackToPlaylistDao().insertTrack(trackToPlaylistEntity)
    }

    override suspend fun deleteTrackFromPlaylist(track: TrackDomainMediaLibrary, playlist: Playlist){
        playlist.addedTracksId.remove(track.trackId)
        playlist.addedTracksNumber--
        val newTracksId = playlist.addedTracksId.toString()
        val addedTracksNumber = playlist.addedTracksNumber
        val playlistId = playlist.playlistId
        appDatabase.playlistDao().changeTracksList(newTracksId, playlistId!!, addedTracksNumber)

        getPlaylists().collect{
            checkTrackInPlaylists(track, it)
        }
    }

    private fun checkTrackInPlaylists(track: TrackDomainMediaLibrary, playlists: List<Playlist>){
            var check = 0
            playlists.forEach {
                if(it.addedTracksId.contains(track.trackId))
                    check++
            }
            if(check == 0){
                val chosenTrackEntity = convertFromTrackDomainMediaLibraryToTrackToPlaylistEntity(track)
                appDatabase.trackToPlaylistDao().deleteTrackEntity(chosenTrackEntity)
            }
    }

    override suspend fun getTracksInPlaylists(): List<TrackDomainMediaLibrary>{
        return appDatabase.trackToPlaylistDao().getTracksInPlaylists().map{ tracksInPlaylists -> convertFromTrackToPlaylistEntityToTrackDomainMediaLibrary(tracksInPlaylists) }
    }

    override suspend fun getTracksInPlaylist(addedTracksId: ArrayList<Long>): ArrayList<TrackDomainMediaLibrary> {
        val tracksInPlaylists = getTracksInPlaylists()
        val tracksInPlaylist = arrayListOf<TrackDomainMediaLibrary>()

        tracksInPlaylists.forEach { if(addedTracksId.contains(it.trackId)) tracksInPlaylist.add(it) }

        return tracksInPlaylist
    }

    private fun convertFromPlaylistEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist{
        return playlistDbConverter.map(playlistEntity)
    }

    private fun convertFromListPlaylistEntityToListPlaylist(playlists: List<PlaylistEntity>): List<Playlist>{
        return playlists.map{ playlist -> playlistDbConverter.map(playlist)}
    }

    private fun convertFromPlaylistToPlaylistEntity(playlist: Playlist): PlaylistEntity{
        return playlistDbConverter.map(playlist)
    }

    private fun convertFromTrackDomainAudioplayerToTrackToPlaylistEntity(track: TrackDomainAudioplayer): TrackToPlaylistEntity{
        return playlistDbConverter.map(track)
    }

    private fun convertFromTrackDomainMediaLibraryToTrackToPlaylistEntity(track: TrackDomainMediaLibrary): TrackToPlaylistEntity{
        return playlistDbConverter.map(track)
    }

    private fun convertFromTrackToPlaylistEntityToTrackDomainMediaLibrary(trackInPlaylist: TrackToPlaylistEntity): TrackDomainMediaLibrary{
        return playlistDbConverter.map(trackInPlaylist)
    }
}