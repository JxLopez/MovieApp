package com.jxlopez.movieapp.data.repository.upload

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage


class UploadRepositoryImpl : UploadRepository {
    private val firebaseStorage = FirebaseStorage.getInstance()
    override fun uploadImageWithUri(uri: Uri, block: ((Result<Uri>, Int) -> Unit)?) {

        val photoRef = firebaseStorage.reference.child("images").child(uri.lastPathSegment!!)
        photoRef.putFile(uri)
            .addOnProgressListener { taskSnapshot ->
                val percentComplete = if (taskSnapshot.totalByteCount > 0) {
                    (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                } else 0

                block?.invoke(Result.Loading, percentComplete)
            }.continueWithTask { task ->
                // Forward any exceptions
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                // Request the public download URL
                photoRef.downloadUrl
            }
            .addOnSuccessListener { block?.invoke(Result.Success(it), 100) }
            .addOnFailureListener { block?.invoke(Result.Error(it), 0) }
    }
}