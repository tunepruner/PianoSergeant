package com.tunepruner.musictraining

import kotlin.math.roundToInt

fun Int.calculateLevelFromPercentage(max: Int, min: Int): Int {
    return ((this.toDouble() / 100) * (max - min) + min).roundToInt()
}

fun <T> List<T>.secondToLastElement(): T? {
    val size = size
    return if (size >= 2) {
        this[size - 2]
    } else null
}