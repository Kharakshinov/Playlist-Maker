package com.practicum.playlistmaker.db.domain

import com.practicum.playlistmaker.audioplayer.domain.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.medialibrary.domain.models.TrackDomainMediaLibrary
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {
    fun getFavouriteTracks(): Flow<List<TrackDomainMediaLibrary>>
    fun getFavouriteTracksId(): Flow<List<Long>>
    suspend fun putTrackInFavourites(track: TrackDomainAudioplayer)
    suspend fun deleteTrackFromFavourites(track: TrackDomainAudioplayer)
}