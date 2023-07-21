package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.domain.db.FavouriteTracksRepository
import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.data.db.converters.TrackDbConverter
import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
): FavouriteTracksRepository {

    override fun getFavouriteTracks(): Flow<List<TrackDomainMediaLibrary>> {
        return appDatabase.trackDao().getTracks().map{ tracks ->  convertFromTrackEntity(tracks)}
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