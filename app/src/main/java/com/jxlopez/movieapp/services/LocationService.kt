package com.jxlopez.movieapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.jxlopez.movieapp.R
import com.jxlopez.movieapp.ui.MainActivity
import com.jxlopez.movieapp.util.Constants.LocationService.ACTION_SHOW_LOCATION_FRAGMENT
import com.jxlopez.movieapp.util.Constants.LocationService.ACTION_START_OR_RESUME_SERVICE
import com.jxlopez.movieapp.util.Constants.LocationService.ACTION_STOP_SERVICE
import com.jxlopez.movieapp.util.Constants.LocationService.FASTEST_LOCATION_INTERVAL
import com.jxlopez.movieapp.util.Constants.LocationService.LOCATION_UPDATE_INTERVAL
import com.jxlopez.movieapp.util.Constants.LocationService.NOTIFICATION_CHANNEL_ID
import com.jxlopez.movieapp.util.Constants.LocationService.NOTIFICATION_CHANNEL_NAME
import com.jxlopez.movieapp.util.Constants.LocationService.NOTIFICATION_ID
import com.jxlopez.movieapp.util.PermissionsUtility

class LocationService : LifecycleService() {
    var isFirstRun = true

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val lastLocation = MutableLiveData<LatLng?>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        lastLocation.postValue(null)
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this) {
            updateLocationTracking(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        isTracking.postValue(true)
                    }
                }
                ACTION_STOP_SERVICE -> {
                    stopService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopService() {
        isFirstRun = true
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            if(PermissionsUtility.hasLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(isTracking.value!!) {
                result?.lastLocation?.let {
                    lastLocation.postValue(LatLng(it.latitude, it.longitude))
                }
            }
        }
    }

    private fun startForegroundService() {
        isTracking.postValue(true)
        lastLocation.postValue(null)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_location_on_24)
            .setContentTitle(getString(R.string.sharing_location_title))
            .setContentText(getString(R.string.sharing_location_description))
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_LOCATION_FRAGMENT
        },
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        } else {
            FLAG_UPDATE_CURRENT
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}