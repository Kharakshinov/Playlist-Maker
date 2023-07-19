package com.practicum.playlistmaker.db.data.converters

import com.practicum.playlistmaker.audioplayer.domain.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.domain.models.TrackDomainMediaLibrary

class TrackDbConverter {

    fun map(track: TrackDomainAudioplayer): TrackEntity {
        return TrackEntity(
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

    fun map(trackEntity: TrackEntity): TrackDomainMediaLibrary {
        return TrackDomainMediaLibrary(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
            trackEntity.timeSaved
        )
    }
}