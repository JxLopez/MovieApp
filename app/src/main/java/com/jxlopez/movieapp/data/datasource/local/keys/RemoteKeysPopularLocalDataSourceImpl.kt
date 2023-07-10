package com.jxlopez.movieapp.data.datasource.local.keys

import com.jxlopez.movieapp.data.db.movies.dao.RemoteKeysPopularDao
import com.jxlopez.movieapp.data.db.movies.dao.RemoteKeysTopRatedDao
import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysPopularEntity
import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysTopRatedEntity
import javax.inject.Inject

class RemoteKeysPopularLocalDataSourceImpl @Inject constructor(
    private val remoteKeysPopularDao: RemoteKeysPopularDao,
    private val remoteKeysTopRatedDao: RemoteKeysTopRatedDao
) : RemoteKeysPopularLocalDataSource {
    override suspend fun insertAll(remoteKey: List<RemoteKeysPopularEntity>) {
        remoteKeysPopularDao.insertAll(remoteKey)
    }

    override suspend fun remoteKeysPopularMovieId(movieId: Int): RemoteKeysPopularEntity? =
        remoteKeysPopularDao.remoteKeysPopularMovieId(movieId)

    override suspend fun clearRemoteKeysPopular() {
        remoteKeysPopularDao.clearRemoteKeysPopular()
    }

    override suspend fun insertTopRatedAll(remoteKey: List<RemoteKeysTopRatedEntity>) =
        remoteKeysTopRatedDao.insertAll(remoteKey)

    override suspend fun remoteKeysTopRatedMovieId(movieId: Int): RemoteKeysTopRatedEntity? =
        remoteKeysTopRatedDao.remoteKeysTopRatedMovieId(movieId)

    override suspend fun clearRemoteKeysTopRated() =
        remoteKeysTopRatedDao.clearRemoteKeysTopRated()
}