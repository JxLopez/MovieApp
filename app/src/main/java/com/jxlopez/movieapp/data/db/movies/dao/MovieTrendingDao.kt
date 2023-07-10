package com.jxlopez.movieapp.data.db.movies.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jxlopez.movieapp.data.db.movies.entities.MovieTrendingEntity

@Dao
interface MovieTrendingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: MovieTrendingEntity)

    @Query("SELECT * FROM movies_trending")
    fun getMoviesTrending(): List<MovieTrendingEntity>

    @Query("DELETE FROM movies")
    fun clearMoviesTrending()
}