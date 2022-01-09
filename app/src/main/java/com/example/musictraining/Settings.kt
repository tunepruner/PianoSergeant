package com.example.musictraining

data class Settings(
    val tempo: Int = 120,
    val chordDistance: Int = 3,
    val beatsPerChord: Int = 4,
    val chordVisible: Boolean = true,
    val modeVisible: Boolean = true,
    val keySigVisible: Boolean = true,
    val cueLineVisible: Boolean = true,
    val currentBeatVisible: Boolean = true,
    val soundOn: Boolean = true,
) {
    val beatDuration: Long get() {
        val millisInAMinute = 60L * 1000
        return millisInAMinute / tempo
    }
}