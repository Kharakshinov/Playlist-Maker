package com.practicum.playlistmaker.db.data.dao

import androidx.room.OnConflictStrategy
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteTrackEntity(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY timeSaved DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT track_id FROM track_table")
    suspend fun getTracksId(): List<Long>
}