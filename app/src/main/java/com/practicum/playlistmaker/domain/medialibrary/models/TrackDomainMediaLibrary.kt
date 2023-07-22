package com.practicum.playlistmaker.domain.medialibrary.models

data class TrackDomainMediaLibrary (
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val timeSaved: Long?
    )