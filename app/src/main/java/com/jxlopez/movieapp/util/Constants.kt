package com.jxlopez.movieapp.util

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w780/"
    const val BASE_POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"
    const val ACCOUNT_ID = 8633584

    const val DATABASE_NAME = "movies.db"
    const val VERSION_DB = 1

    object FirebaseFirestore {
        const val COLLECTION_LOCATION_PATH = "locations"
        const val COLLECTION_IMAGES_PATH = "images"

        const val FIELD_GEOPOINT = "geoPoint"
        const val FIELD_DATE = "date"
    }

    object LocationService {
        const val REQUEST_CODE_LOCATION_PERMISSION = 0

        const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        const val ACTION_SHOW_LOCATION_FRAGMENT = "ACTION_SHOW_LOCATION_FRAGMENT"

        const val LOCATION_UPDATE_INTERVAL = 5*60*1000L
        const val FASTEST_LOCATION_INTERVAL = 3*60*1000L

        const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Tracking"
        const val NOTIFICATION_ID = 1
    }

    object UploadWorker {
        const val ACTION_SHOW_UPLOAD_FRAGMENT = "ACTION_SHOW_UPLOAD_FRAGMENT"
        const val TAG_OUTPUT = "OUTPUT"
        const val IMAGE_CURRENT_WORK_NAME = "image_current_work"
    }
}