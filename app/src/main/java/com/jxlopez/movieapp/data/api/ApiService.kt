package com.jxlopez.movieapp.data.api

import com.jxlopez.movieapp.BuildConfig
import com.jxlopez.movieapp.data.datasource.remote.response.MovieResponse
import com.jxlopez.movieapp.data.datasource.remote.response.ProfileResponse
import com.jxlopez.movieapp.data.datasource.remote.response.RatedResponse
import com.jxlopez.movieapp.util.Constants.ACCOUNT_ID
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 0
    ): MovieResponse

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 0
    ): MovieResponse

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 0
    ): MovieResponse

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendedMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
    ): MovieResponse

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("account/{account_id}")
    suspend fun getInfoProfile(
        @Path("account_id") accountId: Int = ACCOUNT_ID
    ): ProfileResponse

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedByUser(
        @Path("account_id") accountId: Int = ACCOUNT_ID
    ): RatedResponse
}