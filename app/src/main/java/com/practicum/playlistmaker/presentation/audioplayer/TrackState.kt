package com.practicum.playlistmaker.presentation.audioplayer

sealed class TrackState {
    object Liked: TrackState()
    object NotLiked: TrackState()
}