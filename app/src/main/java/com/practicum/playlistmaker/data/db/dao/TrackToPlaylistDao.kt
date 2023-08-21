package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.TrackToPlaylistEntity

@Dao
interface TrackToPlaylistDao {

    @Insert(entity = TrackToPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackToPlaylistEntity)

    @Delete(entity = TrackToPlaylistEntity::class)
    fun deleteTrackEntity(track: TrackToPlaylistEntity)

    @Query("SELECT * FROM track_playlist_table")
    fun getTracksInPlaylists(): List<TrackToPlaylistEntity>
}