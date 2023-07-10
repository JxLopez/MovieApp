package com.jxlopez.movieapp.data.datasource.remote

import com.jxlopez.movieapp.data.api.ApiService
import com.jxlopez.movieapp.data.datasource.remote.response.MovieResponse
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
): MovieRemoteDataSource {
    override suspend fun getTrendingMovies(page: Int): MovieResponse {
        return try {
            apiService.getTrendingMovies(page)
        }catch(e: Exception) {
            throw Exception()
        }
    }

    override suspend fun getPopularMovies(page: Int): MovieResponse =
        apiService.getPopularMovies(page)

    override suspend fun getTopRatedMovies(page: Int): MovieResponse =
        apiService.getTopRatedMovies(page)

    override suspend fun getRecommendedMovies(movieId: Int): MovieResponse {
        return try {
            apiService.getRecommendedMovies(movieId)
        }catch(e: Exception) {
            throw Exception()
        }
    }
}