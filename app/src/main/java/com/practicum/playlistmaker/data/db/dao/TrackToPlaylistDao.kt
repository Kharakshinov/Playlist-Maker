package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.data.db.entity.TrackToPlaylistEntity

@Dao
interface TrackToPlaylistDao {

    @Insert(entity = TrackToPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackToPlaylistEntity)
}