package com.practicum.playlistmaker.audioplayer.presentation

sealed class TrackState {
    object Liked: TrackState()
    object NotLiked: TrackState()
}