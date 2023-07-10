package com.jxlopez.movieapp.data.datasource.local

import com.jxlopez.movieapp.data.db.movies.entities.MovieRatedByUserEntity
import com.jxlopez.movieapp.data.db.movies.entities.ProfileEntity

interface ProfileLocalDataSource {
    suspend fun saveProfile(profile: ProfileEntity)
    suspend fun insertAllRated(moviesRated: List<MovieRatedByUserEntity>)
    fun getProfile(): ProfileEntity?
    fun getMoviesRated(): List<MovieRatedByUserEntity>
    suspend fun clearMoviesRated()
}