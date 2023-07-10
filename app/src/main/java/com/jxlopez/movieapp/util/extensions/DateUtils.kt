package com.jxlopez.movieapp.util.extensions

import com.google.gson.internal.bind.util.ISO8601Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.convertISO8601(): String {
    return ISO8601Utils.format(this)
}

fun String.customFormat(pattern: String = "yyyy-MM-dd'T'HH:mm:ss'Z'"): Date {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.parse(this)
}

fun Date.toHumanReadableDateTime(pattern: String = "EEEE dd, MMMM, hh:mm aa"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}