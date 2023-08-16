package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
): PlaylistsInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun putTrackInPlaylist(track: TrackDomainAudioplayer, playlist: Playlist) {
        playlistsRepository.putTrackInPlaylist(track, playlist)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistsRepository.createPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsRepository.deletePlaylist(playlist)
    }
}