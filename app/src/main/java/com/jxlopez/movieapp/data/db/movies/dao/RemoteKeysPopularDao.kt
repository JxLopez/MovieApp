package com.jxlopez.movieapp.data.db.movies.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysPopularEntity

@Dao
interface RemoteKeysPopularDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<RemoteKeysPopularEntity>)

    @Query("SELECT * FROM remote_keys_popular WHERE movieId = :movieId")
    fun remoteKeysPopularMovieId(movieId: Int): RemoteKeysPopularEntity?

    @Query("DELETE FROM remote_keys_popular")
    fun clearRemoteKeysPopular()
}