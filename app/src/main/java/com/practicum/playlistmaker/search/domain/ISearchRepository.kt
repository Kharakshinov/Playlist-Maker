package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface ISearchRepository{
    fun loadTracks(expression: String): Flow<Resource<List<Track>>>
}