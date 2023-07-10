package com.jxlopez.movieapp.util.extensions

import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.convertDecimal(): String {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.DOWN
    return df.format(this)
}