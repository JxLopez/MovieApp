package com.jxlopez.movieapp.data.datasource.local

import androidx.paging.PagingSource
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity
import com.jxlopez.movieapp.data.db.movies.entities.MovieTrendingEntity

interface MovieLocalDataSource {
    suspend fun insertAll(movies: List<MovieEntity>)
    suspend fun insert(movie: MovieEntity)
    suspend fun insertMovieTrending(movie: MovieTrendingEntity)
    suspend fun getMoviesTrending(): MovieTrendingEntity?
    fun getPagedMoviesPopular(): PagingSource<Int, MovieEntity>
    fun getPagedMoviesTopRated(): PagingSource<Int, MovieEntity>
    fun getMoviesRecommended(): List<MovieEntity>?
    suspend fun clearMoviesPopular()
    suspend fun clearMoviesTopRated()
    suspend fun clearMoviesTrending()
    suspend fun clearMoviesRecommend()
}