package com.jxlopez.movieapp.data.datasource.local

import androidx.paging.PagingSource
import com.jxlopez.movieapp.data.db.movies.dao.MovieDao
import com.jxlopez.movieapp.data.db.movies.dao.MovieTrendingDao
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity
import com.jxlopez.movieapp.data.db.movies.entities.MovieTrendingEntity
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val movieTrendingDao: MovieTrendingDao
) : MovieLocalDataSource {

    override suspend fun insertAll(movies: List<MovieEntity>) {
        movieDao.insertAll(movies)
    }

    override suspend fun insert(movie: MovieEntity) {
        movieDao.saveMovie(movie)
    }

    override suspend fun insertMovieTrending(movie: MovieTrendingEntity) {
        movieTrendingDao.saveMovie(movie)
    }

    override suspend fun getMoviesTrending(): MovieTrendingEntity? {
        val savedMovie = movieTrendingDao.getMoviesTrending()
        return if(savedMovie.isNotEmpty())
            savedMovie.firstOrNull()
        else
            null
    }

    override fun getPagedMoviesPopular(): PagingSource<Int, MovieEntity> =
        movieDao.getPagedMoviesPopular()

    override fun getPagedMoviesTopRated(): PagingSource<Int, MovieEntity> =
        movieDao.getPagedMoviesTopRated()

    override fun getMoviesRecommended(): List<MovieEntity>? {
        return movieDao.getPagedMoviesRecommended()
    }

    override suspend fun clearMoviesPopular() {
        movieDao.clearMoviesPopular()
    }

    override suspend fun clearMoviesTopRated() {
        movieDao.clearMoviesTopRated()
    }

    override suspend fun clearMoviesTrending() {
        movieDao.clearMoviesTrending()
    }

    override suspend fun clearMoviesRecommend() {
        movieDao.clearMoviesTrending()
    }
}