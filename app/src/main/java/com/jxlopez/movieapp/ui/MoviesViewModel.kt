package com.jxlopez.movieapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.jxlopez.movieapp.data.repository.MovieRepository
import com.jxlopez.movieapp.data.db.mapper.toMovieItem
import com.jxlopez.movieapp.data.db.movies.entities.MovieEntity
import com.jxlopez.movieapp.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
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
                Log.e("getMovieTrendingVM:::","$it")
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

    /*init {
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val getListMoviesPopular = actionStateFlow
            .filterIsInstance<UiAction.GetListMoviesPopular>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.GetListMoviesPopular) }
        val listScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll) }

        pagingDataFlow = getListMoviesPopular
            .flatMapLatest { getMoviesPopular2() }
            .cachedIn(viewModelScope)

        state = combine(
            getListMoviesPopular,
            listScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = false
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    private fun getMoviesPopular2(): Flow<PagingData<UiModel>> =
        movieRepository.getMoviesPopular()
            .map { pagingData -> pagingData.map { UiModel.MovieItem(it) } }
            /*.map {
                it.insertSeparators { before, after ->
                    if (after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }

                    if (before == null) {
                        // we're at the beginning of the list
                        return@insertSeparators UiModel.SeparatorItem("${after.roundedStarCount}0.000+ stars")
                    }
                    // check between 2 items
                    if (before.roundedStarCount > after.roundedStarCount) {
                        if (after.roundedStarCount >= 1) {
                            UiModel.SeparatorItem("${after.roundedStarCount}0.000+ stars")
                        } else {
                            UiModel.SeparatorItem("< 10.000+ stars")
                        }
                    } else {
                        // no separator
                        null
                    }
                }
            }*/

     */
}

/*sealed class UiAction {
    object GetListMoviesPopular: UiAction()
    object Scroll: UiAction()
}

data class UiState(
    val hasNotScrolledForCurrentSearch: Boolean = false
)

sealed class UiModel {
    data class MovieItem(val movieEntity: MovieEntity) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}*/
sealed class MovieViewState {
    object Loading: MovieViewState()
    class MovieTrending(val movieTrending: MovieItem?) : MovieViewState()
    class MovieRecommend(val moviesRecommended: List<MovieItem>) : MovieViewState()
    class ErrorTrending(val error: String) : MovieViewState()
}