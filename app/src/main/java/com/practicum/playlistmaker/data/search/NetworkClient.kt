package com.practicum.playlistmaker.data.search

interface NetworkClient {
    suspend fun doRequest(dto: TracksSearchRequest): Response
}