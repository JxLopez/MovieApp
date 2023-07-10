package com.jxlopez.movieapp.data.repository

import androidx.paging.PagingData
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity
import com.jxlopez.movieapp.model.MovieItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesPopular(): Flow<PagingData<MovieEntity>>
    fun getMoviesTopRated(): Flow<PagingData<MovieEntity>>
    fun getMovieTrending(): Flow<MovieItem?>
    fun getMoviesRecommend(movieId: Int): Flow<List<MovieItem>>
}