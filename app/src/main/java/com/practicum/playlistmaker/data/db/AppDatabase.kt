package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.data.db.dao.TrackDao
import com.practicum.playlistmaker.data.db.dao.TrackToPlaylistDao
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.data.db.entity.TrackToPlaylistEntity

@Database(version = 4, entities = [TrackEntity::class, PlaylistEntity::class, TrackToPlaylistEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackToPlaylistDao(): TrackToPlaylistDao
}