package com.practicum.playlistmaker.data.db.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.db.entity.TrackToPlaylistEntity
import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary

class PlaylistDbConverter {

    fun map(playlist: Playlist): PlaylistEntity{
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistImage,
            Gson().toJson(playlist.addedTracksId),
            playlist.addedTracksNumber,
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist{
        val type = object: TypeToken<ArrayList<Long>>() {}.type
        return Playlist(
            playlistEntity.playlistId,
            playlistEntity.playlistName,
            playlistEntity.playlistDescription,
            playlistEntity.playlistImage,
            Gson().fromJson(playlistEntity.addedTracksId, type),
            playlistEntity.addedTracksNumber,
        )
    }

    fun map(track: TrackDomainAudioplayer): TrackToPlaylistEntity {
        return TrackToPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            null
        )
    }

    fun map(trackInPlaylist: TrackToPlaylistEntity): TrackDomainMediaLibrary{
        return TrackDomainMediaLibrary(
            trackInPlaylist.trackId,
            trackInPlaylist.trackName,
            trackInPlaylist.artistName,
            trackInPlaylist.trackTimeMillis,
            trackInPlaylist.artworkUrl100,
            trackInPlaylist.collectionName,
            trackInPlaylist.releaseDate,
            trackInPlaylist.primaryGenreName,
            trackInPlaylist.country,
            trackInPlaylist.previewUrl,
            null
        )
    }
}