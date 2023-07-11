package com.jxlopez.movieapp.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jxlopez.movieapp.data.repository.ProfileRepository
import com.jxlopez.movieapp.model.MovieRatedByUserItem
import com.jxlopez.movieapp.model.ProfileItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<ProfileViewState>()
    fun getViewState() = _viewState

    init {
        getProfile()
        getMoviesRated()
    }

    private fun getProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().collect {
                _viewState.value = ProfileViewState.Profile(it)
            }
        }
    }

    private fun getMoviesRated() {
        viewModelScope.launch {
            profileRepository.getMoviesRated().collect {
                _viewState.value = ProfileViewState.MoviesRated(it)
            }
        }
    }

}

sealed class ProfileViewState {
    class Profile(val profile: ProfileItem?) : ProfileViewState()
    class MoviesRated(val moviesRated: List<MovieRatedByUserItem>) : ProfileViewState()
    class Error(val error: String) : ProfileViewState()
}