package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.domain.search.model.TrackDomainSearch
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {

    override fun loadTracks(expression: String): Flow<Resource<List<TrackDomainSearch>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                with(response as TracksSearchResponse) {
                    val data = results.map {
                        TrackDomainSearch(
                                trackId = it.trackId,
                                trackName = it.trackName,
                                artistName = it.artistName,
                                trackTimeMillis = it.trackTimeMillis,
                                artworkUrl100 = it.artworkUrl100,
                                collectionName = it.collectionName,
                                releaseDate = it.releaseDate,
                                primaryGenreName = it.primaryGenreName,
                                country = it.country,
                                previewUrl = it.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}