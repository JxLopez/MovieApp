package com.jxlopez.movieapp.ui.media

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.jxlopez.movieapp.util.Constants.UploadWorker.IMAGE_CURRENT_WORK_NAME
import com.jxlopez.movieapp.util.Constants.UploadWorker.TAG_OUTPUT
import com.jxlopez.movieapp.workers.ImageUploaderWorker
import com.jxlopez.movieapp.workers.ImageUploaderWorker.Companion.KEY_IMAGE_URI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    application: Application,
) : ViewModel() {
    val imageUri = MutableLiveData<Uri?>()
    private val workManager = WorkManager.getInstance(application.applicationContext)
    internal val outputWorkInfos: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    init {
        imageUri.postValue(null)
    }
    internal fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_CURRENT_WORK_NAME)
    }

    fun startUploadImage() {
        imageUri.value?.let {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val uploadImageWorker: OneTimeWorkRequest = OneTimeWorkRequestBuilder<ImageUploaderWorker>()
                .setConstraints(constraints)
                .setInputData(
                    workDataOf(
                        KEY_IMAGE_URI to it.toString()
                    )
                )
                .addTag(TAG_OUTPUT)
                .build()

            var continuation = workManager
                .beginUniqueWork(
                    IMAGE_CURRENT_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    uploadImageWorker
                )
            continuation.enqueue()

        }
    }
}