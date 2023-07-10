package com.jxlopez.movieapp.model

import com.google.firebase.Timestamp

data class LocationUserItem(
    val latitude: Double,
    val longitude: Double,
    val date: Timestamp? = null
)
