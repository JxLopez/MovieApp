package com.jxlopez.movieapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.LocalCacheSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.jxlopez.movieapp.data.api.ApiService
import com.jxlopez.movieapp.data.datasource.local.MovieLocalDataSource
import com.jxlopez.movieapp.data.datasource.local.MovieLocalDataSourceImpl
import com.jxlopez.movieapp.data.datasource.local.ProfileLocalDataSource
import com.jxlopez.movieapp.data.datasource.local.ProfileLocalDataSourceImpl
import com.jxlopez.movieapp.data.datasource.local.keys.RemoteKeysPopularLocalDataSource
import com.jxlopez.movieapp.data.datasource.local.keys.RemoteKeysPopularLocalDataSourceImpl
import com.jxlopez.movieapp.data.datasource.remote.MovieRemoteDataSource
import com.jxlopez.movieapp.data.datasource.remote.MovieRemoteDataSourceImpl
import com.jxlopez.movieapp.data.datasource.remote.ProfileRemoteDataSource
import com.jxlopez.movieapp.data.datasource.remote.ProfileRemoteDataSourceImpl
import com.jxlopez.movieapp.data.repository.LocationRepository
import com.jxlopez.movieapp.data.repository.LocationRepositoryImpl
import com.jxlopez.movieapp.data.repository.MovieRepository
import com.jxlopez.movieapp.data.repository.MovieRepositoryImpl
import com.jxlopez.movieapp.data.repository.ProfileRepository
import com.jxlopez.movieapp.data.repository.ProfileRepositoryImpl
import com.jxlopez.movieapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    @Named("logging")
    internal fun provideHttpLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideOkHttpClient(@Named("logging") httpLoggingInterceptor: Interceptor) =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl):
            MovieRemoteDataSource = movieRemoteDataSourceImpl

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(movieLocalDataSourceImpl: MovieLocalDataSourceImpl):
            MovieLocalDataSource = movieLocalDataSourceImpl

    @Provides
    @Singleton
    fun provideProfileRemoteDataSource(profileRemoteDataSourceImpl: ProfileRemoteDataSourceImpl):
            ProfileRemoteDataSource = profileRemoteDataSourceImpl

    @Provides
    @Singleton
    fun provideProfileLocalDataSource(profileLocalDataSourceImpl: ProfileLocalDataSourceImpl):
            ProfileLocalDataSource = profileLocalDataSourceImpl

    @Provides
    @Singleton
    fun provideRemoteKeysPopularLocalDataSource(remoteKeysPopularLocalDataSource: RemoteKeysPopularLocalDataSourceImpl):
            RemoteKeysPopularLocalDataSource = remoteKeysPopularLocalDataSource

    @Provides
    @Singleton
    fun provideMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository = movieRepositoryImpl

    @Provides
    @Singleton
    fun provideProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository = profileRepositoryImpl

    @Provides
    @Singleton
    fun provideLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository = locationRepositoryImpl

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore{
        val db = FirebaseFirestore.getInstance()
        val settings = firestoreSettings {
            setLocalCacheSettings(persistentCacheSettings {})
        }
        db.firestoreSettings = settings
        return db
    }
}