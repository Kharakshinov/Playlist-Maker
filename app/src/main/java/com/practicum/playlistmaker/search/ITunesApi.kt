package com.practicum.playlistmaker.search

import com.practicum.playlistmaker.search.model.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<TracksResponse>
}