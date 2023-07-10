package com.jxlopez.movieapp.data.datasource.remote

import com.jxlopez.movieapp.data.api.ApiService
import com.jxlopez.movieapp.data.datasource.remote.response.ProfileResponse
import com.jxlopez.movieapp.data.datasource.remote.response.RatedResponse
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
): ProfileRemoteDataSource {
    override suspend fun getInfoProfile(): ProfileResponse =
        apiService.getInfoProfile()

    override suspend fun getRatedByUser(): RatedResponse =
        apiService.getRatedByUser()
}