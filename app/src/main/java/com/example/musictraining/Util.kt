package com.example.musictraining

import kotlin.math.roundToInt

fun calculateLevelFromPercentage(percentage: Int, max: Int, min: Int): Int {
    return ((percentage.toDouble() / 100) * (max - min) + min).roundToInt()
}