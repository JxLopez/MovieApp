package com.jxlopez.movieapp.data.repository.request

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class LocationUserRequest(
    val geoPoint: GeoPoint? = null,
    val date: Timestamp? = null
)
