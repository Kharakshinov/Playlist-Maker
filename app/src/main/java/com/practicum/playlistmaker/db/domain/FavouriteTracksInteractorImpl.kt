package com.practicum.playlistmaker.db.domain

import com.practicum.playlistmaker.audioplayer.domain.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.medialibrary.domain.models.TrackDomainMediaLibrary
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val favouriteTracksRepository: FavouriteTracksRepository
): FavouriteTracksInteractor {
     override fun getFavouriteTracks(): Flow<List<TrackDomainMediaLibrary>> {
        return favouriteTracksRepository.getFavouriteTracks()
    }

    override fun getFavouriteTracksId(): Flow<List<Long>> {
        return favouriteTracksRepository.getFavouriteTracksId()
    }

    override suspend fun putTrackInFavourites(track: TrackDomainAudioplayer) {
        favouriteTracksRepository.putTrackInFavourites(track)
    }

    override suspend fun deleteTrackFromFavourites(track: TrackDomainAudioplayer) {
        favouriteTracksRepository.deleteTrackFromFavourites(track)
    }

}