package com.jxlopez.movieapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jxlopez.movieapp.data.db.movies.dao.MovieDao
import com.jxlopez.movieapp.data.db.movies.dao.MovieRatedByUserDao
import com.jxlopez.movieapp.data.db.movies.dao.MovieTrendingDao
import com.jxlopez.movieapp.data.db.movies.dao.ProfileDao
import com.jxlopez.movieapp.data.db.movies.dao.RemoteKeysPopularDao
import com.jxlopez.movieapp.data.db.movies.dao.RemoteKeysTopRatedDao
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity
import com.jxlopez.movieapp.data.db.movies.entities.MovieRatedByUserEntity
import com.jxlopez.movieapp.data.db.movies.entities.MovieTrendingEntity
import com.jxlopez.movieapp.data.db.movies.entities.ProfileEntity
import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysPopularEntity
import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysTopRatedEntity
import com.jxlopez.movieapp.ui.MovieViewState
import com.jxlopez.movieapp.util.Constants

@Database(
    entities = [
        MovieEntity::class,
        MovieTrendingEntity::class,
        ProfileEntity::class,
        MovieRatedByUserEntity::class,
        RemoteKeysPopularEntity::class,
        RemoteKeysTopRatedEntity::class,
    ],
    version = Constants.VERSION_DB
)
internal abstract class MovieDataBase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val movieTrendingDao: MovieTrendingDao
    abstract val profileDao: ProfileDao
    abstract val movieRatedByUserDao: MovieRatedByUserDao
    abstract val remoteKeysPopularDao: RemoteKeysPopularDao
    abstract val remoteKeysTopRatedDao: RemoteKeysTopRatedDao
}