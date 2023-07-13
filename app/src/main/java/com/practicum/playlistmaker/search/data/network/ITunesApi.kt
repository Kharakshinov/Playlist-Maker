package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") expression: String): TracksSearchResponse
}