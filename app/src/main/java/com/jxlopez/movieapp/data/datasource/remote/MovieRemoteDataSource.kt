package com.jxlopez.movieapp.data.datasource.remote

import com.jxlopez.movieapp.data.datasource.remote.response.MovieResponse

interface MovieRemoteDataSource {
    suspend fun getTrendingMovies(page: Int): MovieResponse
    suspend fun getPopularMovies(page: Int): MovieResponse
    suspend fun getTopRatedMovies(page: Int): MovieResponse
    suspend fun getRecommendedMovies(movieId: Int): MovieResponse
}