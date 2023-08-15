package com.practicum.playlistmaker.data.db.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist

class PlaylistDbConverter {

    fun map(playlist: Playlist): PlaylistEntity{
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistImage,
            Gson().toJson(playlist.addedTracksId),
            playlist.addedTracksNumber,
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist{
        val type = object: TypeToken<List<Long>>() {}.type
        return Playlist(
            playlistEntity.playlistId,
            playlistEntity.playlistName,
            playlistEntity.playlistDescription,
            playlistEntity.playlistImage,
            Gson().fromJson(playlistEntity.addedTracksId, type),
            playlistEntity.addedTracksNumber,
        )
    }
}