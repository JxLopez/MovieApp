package com.jxlopez.movieapp.data.repository

import com.jxlopez.movieapp.data.datasource.local.ProfileLocalDataSource
import com.jxlopez.movieapp.data.datasource.remote.ProfileRemoteDataSource
import com.jxlopez.movieapp.data.db.mapper.toMovieRatedByUserEntity
import com.jxlopez.movieapp.data.db.mapper.toMovieRatedByUserItem
import com.jxlopez.movieapp.data.db.mapper.toProfileEntity
import com.jxlopez.movieapp.data.db.mapper.toProfileItem
import com.jxlopez.movieapp.model.MovieRatedByUserItem
import com.jxlopez.movieapp.model.ProfileItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource
) : ProfileRepository {
    override fun getProfile(): Flow<ProfileItem?> =
        flow {
            try {
                val result = profileRemoteDataSource.getInfoProfile()
                val profileEntity = result.toProfileEntity()
                profileEntity?.let {
                    profileLocalDataSource.saveProfile(profileEntity)
                    val movieItem = it.toProfileItem()
                    emit(movieItem)
                }
            } catch (e: Exception) {
                val movieLocal = profileLocalDataSource.getProfile()
                val movieItem = movieLocal?.toProfileItem()
                emit(movieItem)
            }
        }

    override fun getMoviesRated(): Flow<List<MovieRatedByUserItem>> =
        flow {
            try {
                val result = profileRemoteDataSource.getRatedByUser()
                val movieRatedEntity = result.results.map { it.toMovieRatedByUserEntity() }
                movieRatedEntity?.let {
                    profileLocalDataSource.insertAllRated(movieRatedEntity)
                    val movieRatedItem = it.map{ it.toMovieRatedByUserItem() }
                    emit(movieRatedItem)
                }
            } catch (e: Exception) {
                val movieRatedLocal = profileLocalDataSource.getMoviesRated()
                val movieRatedItem = movieRatedLocal.map { it.toMovieRatedByUserItem() }
                emit(movieRatedItem)
            }
        }
}