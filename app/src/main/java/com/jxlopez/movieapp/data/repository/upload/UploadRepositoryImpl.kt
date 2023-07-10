package com.jxlopez.movieapp.data.repository.upload

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.jxlopez.movieapp.util.Constants.FirebaseFirestore.COLLECTION_IMAGES_PATH


class UploadRepositoryImpl : UploadRepository {
    private val firebaseStorage = FirebaseStorage.getInstance()
    override fun uploadImageWithUri(uri: Uri, block: ((Result<Uri>, Int) -> Unit)?) {
        for (i in 0..3) {
            val photoRef = firebaseStorage.reference.child(COLLECTION_IMAGES_PATH).child(uri.lastPathSegment!!)
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
}