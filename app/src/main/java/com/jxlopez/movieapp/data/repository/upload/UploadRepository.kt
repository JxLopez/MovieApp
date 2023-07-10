package com.jxlopez.movieapp.data.repository.upload

import android.net.Uri

interface UploadRepository {
    fun uploadImageWithUri(uri: Uri,
                                   block: ((Result<Uri>, Int) -> Unit)?)
}