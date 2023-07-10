package com.jxlopez.movieapp.data.repository

import com.jxlopez.movieapp.model.LocationUserItem

interface LocationRepository {
    suspend fun saveLocation(latitude: Double, longitude: Double): Boolean
    suspend fun getLocations(): List<LocationUserItem>
}