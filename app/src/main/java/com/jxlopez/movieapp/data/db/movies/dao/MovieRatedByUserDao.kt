package com.jxlopez.movieapp.data.db.movies.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jxlopez.movieapp.data.db.movies.entities.MovieRatedByUserEntity

@Dao
interface MovieRatedByUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(moviesRated: List<MovieRatedByUserEntity>)

    @Transaction
    @Query("SELECT * FROM movie_rated_user")
    fun getMoviesRated(): List<MovieRatedByUserEntity>

    @Query("DELETE FROM movie_rated_user")
    fun clearMoviesRated()
}