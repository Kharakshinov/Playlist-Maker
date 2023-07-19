package com.practicum.playlistmaker.db.data

import com.practicum.playlistmaker.db.domain.FavouriteTracksRepository
import com.practicum.playlistmaker.audioplayer.domain.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.db.AppDatabase
import com.practicum.playlistmaker.db.data.converters.TrackDbConverter
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.domain.models.TrackDomainMediaLibrary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
): FavouriteTracksRepository {
    override fun getFavouriteTracks(): Flow<List<TrackDomainMediaLibrary>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getFavouriteTracksId(): Flow<List<Long>> = flow {
        val tracksId = appDatabase.trackDao().getTracksId()
        emit((tracksId))
    }

    override suspend fun putTrackInFavourites(track: TrackDomainAudioplayer) {
        val trackEntity = convertToTrackEntity(track)
        trackEntity.timeSaved = Date().time
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrackFromFavourites(track: TrackDomainAudioplayer) {
        val trackEntity = convertToTrackEntity(track)
        appDatabase.trackDao().deleteTrackEntity(trackEntity)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackDomainMediaLibrary> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

    private fun convertToTrackEntity(track: TrackDomainAudioplayer): TrackEntity {
        return trackDbConverter.map(track)
    }
}