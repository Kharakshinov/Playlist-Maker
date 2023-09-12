package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
): PlaylistsInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistsRepository.createPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Int?) {
        playlistsRepository.deletePlaylist(playlistId)
    }

    override suspend fun editPlaylist(
        playlistId: Int?,
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
        playlistsRepository.editPlaylist(
            playlistId,
            playlistName,
            playlistDescription,
            playlistImage
        )
    }

    override suspend fun getPlaylist(playlistId: Int?): Playlist{
       return playlistsRepository.getPlaylist(playlistId)
    }

    override suspend fun putTrackInPlaylist(track: TrackDomainAudioplayer, playlist: Playlist) {
        playlistsRepository.putTrackInPlaylist(track, playlist)
    }

    override suspend fun deleteTrackFromPlaylist(chosenTrack: TrackDomainMediaLibrary, playlist: Playlist){
        playlistsRepository.deleteTrackFromPlaylist(chosenTrack, playlist)
    }

    override suspend fun getTracksInPlaylistWithId(addedTracksId: ArrayList<Long>): ArrayList<TrackDomainMediaLibrary> {
        return playlistsRepository.getTracksInPlaylistWithId(addedTracksId)
    }
}