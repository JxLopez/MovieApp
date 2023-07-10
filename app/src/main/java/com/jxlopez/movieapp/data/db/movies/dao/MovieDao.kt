package com.jxlopez.movieapp.data.db.movies.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: MovieEntity)

    @Transaction
    @Query("SELECT * FROM movies where sectionType = 'popular' order by popularity desc")
    fun getPagedMoviesPopular(): PagingSource<Int, MovieEntity>

    @Transaction
    @Query("SELECT * FROM movies where sectionType = 'top_rated' order by voteAverage desc")
    fun getPagedMoviesTopRated(): PagingSource<Int, MovieEntity>

    @Transaction
    @Query("SELECT * FROM movies where sectionType = 'recommended'")
    fun getPagedMoviesRecommended(): List<MovieEntity>

    @Query("DELETE FROM movies where sectionType = 'popular'")
    fun clearMoviesPopular()

    @Query("DELETE FROM movies where sectionType = 'top_rated'")
    fun clearMoviesTopRated()

    @Query("DELETE FROM movies where sectionType = 'trending'")
    fun clearMoviesTrending()

    @Query("DELETE FROM movies where sectionType = 'recommended'")
    fun clearMoviesRecommended()
}