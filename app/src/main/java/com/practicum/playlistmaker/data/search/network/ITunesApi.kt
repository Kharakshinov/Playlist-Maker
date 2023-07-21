package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.search.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") expression: String): TracksSearchResponse
}