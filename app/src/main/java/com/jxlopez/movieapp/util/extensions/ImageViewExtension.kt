package com.jxlopez.movieapp.util.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jxlopez.movieapp.R

fun ImageView.loadImageUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .error(R.drawable.ic_placeholder_image)
        .placeholder(R.drawable.ic_placeholder_image)
        .into(this)
}

fun ImageView.setLocalImage(uri: Uri, applyCircle: Boolean = false) {
    val glide = Glide.with(this).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE)
    if (applyCircle) {
        glide.apply(RequestOptions.circleCropTransform()).into(this)
    } else {
        glide.into(this)
    }
}