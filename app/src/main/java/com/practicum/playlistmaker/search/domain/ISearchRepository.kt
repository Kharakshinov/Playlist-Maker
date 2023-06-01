package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track

interface ISearchRepository{
    fun loadTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit)
}