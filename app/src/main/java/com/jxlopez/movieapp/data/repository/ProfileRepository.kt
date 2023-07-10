package com.jxlopez.movieapp.data.repository

import com.jxlopez.movieapp.model.MovieRatedByUserItem
import com.jxlopez.movieapp.model.ProfileItem
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<ProfileItem?>
    fun getMoviesRated(): Flow<List<MovieRatedByUserItem>>
}