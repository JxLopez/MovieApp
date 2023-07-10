package com.jxlopez.movieapp.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jxlopez.movieapp.data.datasource.MovieRemoteMediator
import com.jxlopez.movieapp.data.datasource.MovieTopRatedRemoteMediator
import com.jxlopez.movieapp.data.datasource.local.MovieLocalDataSource
import com.jxlopez.movieapp.data.datasource.local.keys.RemoteKeysPopularLocalDataSource
import com.jxlopez.movieapp.data.datasource.remote.MovieRemoteDataSource
import com.jxlopez.movieapp.data.db.mapper.toMovieEntity
import com.jxlopez.movieapp.data.db.mapper.toMovieItem
import com.jxlopez.movieapp.data.db.mapper.toMovieTrendingEntity
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity
import com.jxlopez.movieapp.model.MovieItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val remoteKeysPopularLocalDataSource: RemoteKeysPopularLocalDataSource
) : MovieRepository {
    override fun getMoviesPopular(): Flow<PagingData<MovieEntity>> {
        val pagingSourceMoviesFactory = { movieLocalDataSource.getPagedMoviesPopular() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = MovieRemoteMediator(
                movieRemoteDataSource,
                movieLocalDataSource,
                remoteKeysPopularLocalDataSource
            ),
            pagingSourceFactory = pagingSourceMoviesFactory
        ).flow
    }

    override fun getMoviesTopRated(): Flow<PagingData<MovieEntity>> {
        val pagingSourceMoviesFactory = { movieLocalDataSource.getPagedMoviesTopRated() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = MovieTopRatedRemoteMediator(
                movieRemoteDataSource,
                movieLocalDataSource,
                remoteKeysPopularLocalDataSource
            ),
            pagingSourceFactory = pagingSourceMoviesFactory
        ).flow
    }

    override fun getMovieTrending(): Flow<MovieItem?> =
        flow {
            try {
                val result = movieRemoteDataSource.getTrendingMovies(1)
                if(result.results.isNotEmpty()) {
                    movieLocalDataSource.clearMoviesTrending()
                    val movieEntity = result.results.firstOrNull()?.toMovieTrendingEntity()
                    movieEntity?.let {
                        movieLocalDataSource.insertMovieTrending(it)
                        val movieItem = it.toMovieItem()
                        Log.e("getMovieTrending:::"," Save $movieItem")
                        emit(movieItem)
                    }
                } else {
                    val movieLocal = movieLocalDataSource.getMoviesTrending()
                    emit(movieLocal?.toMovieItem())
                }
            } catch (e: Exception) {
                val movieLocal = movieLocalDataSource.getMoviesTrending()
                val movieItem = movieLocal?.toMovieItem()
                emit(movieItem)
            }
        }

    override fun getMoviesRecommend(movieId: Int): Flow<List<MovieItem>> =
        flow {
            try {
                val result = movieRemoteDataSource.getRecommendedMovies(movieId)
                if(result.results.isNotEmpty()) {
                    movieLocalDataSource.clearMoviesRecommend()
                    val moviesEntity = result.results.map { it.toMovieEntity("recommended") }
                    movieLocalDataSource.insertAll(moviesEntity)
                    emit(moviesEntity.map { it.toMovieItem() })
                } else {
                    val moviesLocal = movieLocalDataSource.getMoviesRecommended()
                    moviesLocal?.let { emit(it.map { it.toMovieItem() }) }
                }
            } catch (e: Exception) {
                val moviesLocal = movieLocalDataSource.getMoviesRecommended()
                moviesLocal?.let { emit(it.map { it.toMovieItem() }) }
            }
        }
}