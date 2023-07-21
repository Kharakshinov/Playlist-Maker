package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    fun getFavouriteTracks(): Flow<List<TrackDomainMediaLibrary>>
    fun getFavouriteTracksId(): Flow<List<Long>>
    suspend fun putTrackInFavourites(track: TrackDomainAudioplayer)
    suspend fun deleteTrackFromFavourites(track: TrackDomainAudioplayer)
}