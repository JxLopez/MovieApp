package com.jxlopez.movieapp.data.datasource.local.keys

import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysPopularEntity
import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysTopRatedEntity

interface RemoteKeysPopularLocalDataSource {
    suspend fun insertAll(remoteKey: List<RemoteKeysPopularEntity>)
    suspend fun remoteKeysPopularMovieId(movieId: Int): RemoteKeysPopularEntity?
    suspend fun clearRemoteKeysPopular()

    suspend fun insertTopRatedAll(remoteKey: List<RemoteKeysTopRatedEntity>)
    suspend fun remoteKeysTopRatedMovieId(movieId: Int): RemoteKeysTopRatedEntity?
    suspend fun clearRemoteKeysTopRated()
}