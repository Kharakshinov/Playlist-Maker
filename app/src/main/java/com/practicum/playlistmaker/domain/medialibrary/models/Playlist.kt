package com.practicum.playlistmaker.domain.medialibrary.models

data class Playlist (
    val playlistId: Int?,
    val playlistName: String,
    val playlistDescription: String?,
    val playlistImage: String?,
    var addedTracksId: ArrayList<Long>,
    var addedTracksNumber: Int,
)