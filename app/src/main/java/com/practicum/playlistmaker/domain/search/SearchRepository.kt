package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.TrackDomainSearch
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository{
    fun loadTracks(expression: String): Flow<Resource<List<TrackDomainSearch>>>
}