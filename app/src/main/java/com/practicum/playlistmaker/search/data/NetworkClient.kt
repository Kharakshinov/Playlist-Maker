package com.practicum.playlistmaker.search.data

interface NetworkClient {
    suspend fun doRequest(dto: TracksSearchRequest): Response
}