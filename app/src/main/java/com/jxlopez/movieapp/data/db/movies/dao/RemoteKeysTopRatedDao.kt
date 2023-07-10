package com.jxlopez.movieapp.data.db.movies.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysTopRatedEntity

@Dao
interface RemoteKeysTopRatedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<RemoteKeysTopRatedEntity>)

    @Query("SELECT * FROM remote_keys_top_rated WHERE movieId = :movieId")
    fun remoteKeysTopRatedMovieId(movieId: Int): RemoteKeysTopRatedEntity?

    @Query("DELETE FROM remote_keys_top_rated")
    fun clearRemoteKeysTopRated()
}