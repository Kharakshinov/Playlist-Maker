package com.practicum.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlist_id")
    val playlistId: Int?,
    val playlistName: String,
    val playlistDescription: String?,
    val playlistImage: String?,
    var addedTracksId: String?,
    var addedTracksNumber: Int = 0,
)