package com.jxlopez.movieapp.ui.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.jxlopez.movieapp.data.repository.MovieRepository
import com.jxlopez.movieapp.data.db.mapper.toMovieItem
import com.jxlopez.movieapp.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _viewState = MutableLiveData<MovieViewState>()
    fun getViewState() = _viewState

    private val movieId = MutableLiveData<Int>()

    fun getMovieTrending() {
        viewModelScope.launch {
            movieRepository.getMovieTrending().collect {
                _viewState.value = MovieViewState.MovieTrending(it)
                movieId.value = it?.id
                getMoviesRecommended()
            }
        }
    }

    fun getMoviesPopular() : Flow<PagingData<MovieItem>> =
        movieRepository.getMoviesPopular()
            .map { pagingData -> pagingData.map { it.toMovieItem() } }

    fun getMoviesTopRated() : Flow<PagingData<MovieItem>> =
        movieRepository.getMoviesTopRated()
            .map { pagingData -> pagingData.map { it.toMovieItem() } }

    private fun getMoviesRecommended() {
        viewModelScope.launch {
            movieId.value?.let {
                movieRepository.getMoviesRecommend(it).collect {
                    _viewState.value = MovieViewState.MovieRecommend(it)
                }
            }
        }
    }
}

sealed class MovieViewState {
    object Loading: MovieViewState()
    class MovieTrending(val movieTrending: MovieItem?) : MovieViewState()
    class MovieRecommend(val moviesRecommended: List<MovieItem>) : MovieViewState()
    class ErrorTrending(val error: String) : MovieViewState()
}