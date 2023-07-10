package com.jxlopez.movieapp.data.datasource.local

import com.jxlopez.movieapp.data.db.movies.dao.MovieRatedByUserDao
import com.jxlopez.movieapp.data.db.movies.dao.ProfileDao
import com.jxlopez.movieapp.data.db.movies.entities.MovieRatedByUserEntity
import com.jxlopez.movieapp.data.db.movies.entities.ProfileEntity
import javax.inject.Inject

class ProfileLocalDataSourceImpl @Inject constructor(
    private val profileDao: ProfileDao,
    private val movieRatedByUserDao: MovieRatedByUserDao
) : ProfileLocalDataSource {
    override suspend fun saveProfile(profile: ProfileEntity) {
        profileDao.saveProfile(profile)
    }

    override suspend fun insertAllRated(moviesRated: List<MovieRatedByUserEntity>) {
        movieRatedByUserDao.insertAll(moviesRated)
    }

    override fun getProfile(): ProfileEntity? {
        val profileEntity = profileDao.getProfile()
        return if(profileEntity.isNotEmpty())
            profileEntity.firstOrNull()
        else
            null
    }

    override fun getMoviesRated(): List<MovieRatedByUserEntity> =
        movieRatedByUserDao.getMoviesRated()

    override suspend fun clearMoviesRated() {
        TODO("Not yet implemented")
    }

}