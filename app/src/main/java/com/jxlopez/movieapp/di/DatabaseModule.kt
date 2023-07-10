package com.jxlopez.movieapp.di

import android.content.Context
import androidx.room.Room
import com.jxlopez.movieapp.data.db.MovieDataBase
import com.jxlopez.movieapp.data.db.movies.dao.MovieDao
import com.jxlopez.movieapp.data.db.movies.dao.MovieRatedByUserDao
import com.jxlopez.movieapp.data.db.movies.dao.MovieTrendingDao
import com.jxlopez.movieapp.data.db.movies.dao.ProfileDao
import com.jxlopez.movieapp.data.db.movies.dao.RemoteKeysPopularDao
import com.jxlopez.movieapp.data.db.movies.dao.RemoteKeysTopRatedDao
import com.jxlopez.movieapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDataBase {
        return Room.databaseBuilder(
            context,
            MovieDataBase::class.java,
            Constants.DATABASE_NAME
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesDao(movieDataBase: MovieDataBase): MovieDao {
        return movieDataBase.movieDao
    }

    @Provides
    @Singleton
    fun provideMoviesTrendingDao(movieDataBase: MovieDataBase): MovieTrendingDao {
        return movieDataBase.movieTrendingDao
    }

    @Provides
    @Singleton
    fun provideProfileDao(movieDataBase: MovieDataBase): ProfileDao {
        return movieDataBase.profileDao
    }

    @Provides
    @Singleton
    fun provideMovieRatedDao(movieDataBase: MovieDataBase): MovieRatedByUserDao {
        return movieDataBase.movieRatedByUserDao
    }

    @Provides
    @Singleton
    fun provideRemoteKeysMoviePopularDao(movieDataBase: MovieDataBase): RemoteKeysPopularDao {
        return movieDataBase.remoteKeysPopularDao
    }

    @Provides
    @Singleton
    fun provideRemoteKeysMovieTopRatedDao(movieDataBase: MovieDataBase): RemoteKeysTopRatedDao {
        return movieDataBase.remoteKeysTopRatedDao
    }

    /*@Provides
    @Singleton
    fun provideJsonConverter(moshi: Moshi): JsonConverter = JsonConverter(moshi)*/
}