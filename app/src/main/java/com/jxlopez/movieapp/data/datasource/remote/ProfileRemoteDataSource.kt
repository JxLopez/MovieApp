package com.jxlopez.movieapp.data.datasource.remote

import com.jxlopez.movieapp.data.datasource.remote.response.ProfileResponse
import com.jxlopez.movieapp.data.datasource.remote.response.RatedResponse

interface ProfileRemoteDataSource {
    suspend fun getInfoProfile(): ProfileResponse
    suspend fun getRatedByUser(): RatedResponse
}