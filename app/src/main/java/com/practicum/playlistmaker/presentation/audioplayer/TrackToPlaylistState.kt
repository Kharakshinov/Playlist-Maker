package com.practicum.playlistmaker.presentation.audioplayer

import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist

sealed class TrackToPlaylistState {
    class InPlaylist(val playlistName: String): TrackToPlaylistState()
    class NotInPlaylist(val track: TrackDomainAudioplayer, val playlist: Playlist): TrackToPlaylistState()
    class SuccessfulAdd(val playlistName: String): TrackToPlaylistState()
}