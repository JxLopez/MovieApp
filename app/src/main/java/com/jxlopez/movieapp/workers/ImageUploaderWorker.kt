package com.jxlopez.movieapp.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.jxlopez.movieapp.R
import com.jxlopez.movieapp.data.repository.upload.UploadRepositoryImpl
import com.jxlopez.movieapp.ui.MainActivity
import com.jxlopez.movieapp.util.Constants
import com.jxlopez.movieapp.util.Notifier
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageUploaderWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val context = applicationContext
    private var title = "Upload Image"
    private val uploadRepository = UploadRepositoryImpl()

    override suspend fun doWork(): Result {
        val fileUri = inputData.getString(KEY_IMAGE_URI)?.toUri()
        fileUri?.let { return uploadImageFromURI(it) }
        throw IllegalStateException("Image URI doesn't exist")
    }

    private suspend fun uploadImageFromURI(fileUri: Uri): Result = suspendCoroutine { cont ->
        uploadRepository.uploadImageWithUri(fileUri) { result, percentage ->
            when (result) {
                is com.jxlopez.movieapp.data.repository.upload.Result.Error -> {
                    showUploadFinishedNotification(null)
                    cont.resume(Result.failure())
                }
                is com.jxlopez.movieapp.data.repository.upload.Result.Loading -> {
                    showProgressNotification(
                        context.getString(R.string.uploading_media_fragment),
                        percentage
                    )
                }
                is com.jxlopez.movieapp.data.repository.upload.Result.Success -> {
                    showUploadFinishedNotification(result.data)

                    val data = Data.Builder()
                        .putAll(inputData)
                        .putString(KEY_UPLOADED_URI, result.data.toString())

                    cont.resume(Result.success(data.build()))
                }
            }
        }
    }

    private fun showProgressNotification(caption: String, percent: Int) {
        Notifier
            .progressable(
                context,
                100, percent
            ) {
                notificationId = title.hashCode()
                contentTitle = title
                contentText = caption
                smallIcon = R.drawable.ic_backup_24
            }
    }

    private fun showUploadFinishedNotification(downloadUrl: Uri?) {
        Notifier
            .dismissNotification(context, title.hashCode())

        if (downloadUrl != null) return

        val caption = context.getString(
            R.string.error_media_fragment
        )
        val intent = Intent(applicationContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent =  PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).also {
                it.action = Constants.UploadWorker.ACTION_SHOW_UPLOAD_FRAGMENT
            },
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )


        Notifier.show(context) {
            notificationId = title.hashCode()
            contentTitle = title
            contentText = caption
            this.pendingIntent = pendingIntent
        }
    }

    companion object {
        const val KEY_IMAGE_URI: String = "key-image-uri"
        const val KEY_UPLOADED_URI: String = "key-uploaded-uri"
    }
}