package com.jxlopez.movieapp.util.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun <T : Any, L : LiveData<T>?> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) {
    removeObservers(liveData)
    liveData?.observe(this, Observer(body))
}

fun <T : Any, L : LiveData<T>?> LifecycleOwner.removeObservers(liveData: L) {
    liveData?.removeObservers(this)
}

fun Fragment.bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}