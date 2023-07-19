package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.TrackDomainSearch
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository{
    fun loadTracks(expression: String): Flow<Resource<List<TrackDomainSearch>>>
}