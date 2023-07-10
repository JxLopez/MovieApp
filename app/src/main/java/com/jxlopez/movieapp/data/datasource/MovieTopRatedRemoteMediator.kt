package com.jxlopez.movieapp.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.jxlopez.movieapp.data.datasource.local.MovieLocalDataSource
import com.jxlopez.movieapp.data.datasource.local.keys.RemoteKeysPopularLocalDataSource
import com.jxlopez.movieapp.data.datasource.remote.MovieRemoteDataSource
import com.jxlopez.movieapp.data.db.mapper.toMovieEntity
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity
import com.jxlopez.movieapp.data.db.movies.entities.RemoteKeysTopRatedEntity

import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieTopRatedRemoteMediator(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val remoteKeysLocalDataSource: RemoteKeysPopularLocalDataSource,
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: TMDB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val apiResponse = movieRemoteDataSource.getTopRatedMovies(page)

            val movies = apiResponse.results
            val endOfPaginationReached = movies.isEmpty()
            if (loadType == LoadType.REFRESH) {
                remoteKeysLocalDataSource.clearRemoteKeysTopRated()
                movieLocalDataSource.clearMoviesTopRated()
            }
            val prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = movies.map {
                RemoteKeysTopRatedEntity(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
            }
            remoteKeysLocalDataSource.insertTopRatedAll(keys)
            movieLocalDataSource.insertAll(movies.map { it.toMovieEntity("top_rated") })
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysTopRatedEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
                remoteKeysLocalDataSource.remoteKeysTopRatedMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysTopRatedEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                // Get the remote keys of the first items retrieved
                remoteKeysLocalDataSource.remoteKeysTopRatedMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieEntity>
    ): RemoteKeysTopRatedEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                remoteKeysLocalDataSource.remoteKeysTopRatedMovieId(movieId)
            }
        }
    }
}