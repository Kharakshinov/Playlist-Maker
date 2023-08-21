package com.practicum.playlistmaker.data.db.dao

import androidx.room.*
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlist_table WHERE playlist_id = :id")
    fun deletePlaylistEntity(id: Int?)

    @Query("SELECT * FROM playlist_table WHERE playlist_id = :id")
    fun getPlaylistEntity(id: Int?): PlaylistEntity

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("UPDATE playlist_table SET addedTracksId = :newTracksId, addedTracksNumber = :number WHERE playlist_id = :id")
    fun changeTracksList(newTracksId: String, id: Int, number: Int)

}