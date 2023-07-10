package com.jxlopez.movieapp.ui.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jxlopez.movieapp.data.repository.LocationRepository
import com.jxlopez.movieapp.model.LocationUserItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel  @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<LocationViewState>()
    fun getViewState() = _viewState

    fun getLocations() {
        viewModelScope.launch {
            val locations = locationRepository.getLocations()
            _viewState.value = LocationViewState.Locations(locations)
        }
    }

    fun saveLocation(latLng: LatLng) {
        viewModelScope.launch {
            val isSavedLocation = locationRepository.saveLocation(latLng.latitude, latLng.longitude)
            if(isSavedLocation)
                getLocations()
        }
    }
}

sealed class LocationViewState {
    class Locations(val locations: List<LocationUserItem>) : LocationViewState()
}