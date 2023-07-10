package com.jxlopez.movieapp.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Source
import com.jxlopez.movieapp.data.repository.request.LocationUserRequest
import com.jxlopez.movieapp.model.LocationUserItem
import com.jxlopez.movieapp.util.Constants
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : LocationRepository {
    override suspend fun saveLocation(latitude: Double, longitude: Double): Boolean {
        val locationUser = LocationUserRequest(
            GeoPoint(latitude, longitude),
            Timestamp(Date())
        )
        return try {
            firestore.collection(Constants.FirebaseFirestore.COLLECTION_LOCATION_PATH)
                .add(locationUser).await()
            true
        } catch (e: FirebaseFirestoreException) {
            false
        }
    }

    override suspend fun getLocations(): List<LocationUserItem> {
        val locationRef =
            firestore.collection(Constants.FirebaseFirestore.COLLECTION_LOCATION_PATH)
        //val source = Source.CACHE
        val locations = locationRef.get().await()
        if(!locations.isEmpty) {
            return locations.documents.map {
                val geoPoint = it.getGeoPoint("geoPoint")
                LocationUserItem(
                    geoPoint?.latitude ?: 0.0,
                    geoPoint?.longitude ?: 0.0,
                    it.getTimestamp("date")
                )
            }
        }
        return listOf()
    }
}