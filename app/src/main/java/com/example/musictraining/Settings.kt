package com.example.musictraining

import kotlinx.coroutines.flow.MutableStateFlow

class Settings(
    tempo: Int = 120,
    var chordDistance: Int = 3,
    var beatsPerChord: Int = 4,
    var chordVisible: Boolean = true,
    var modeVisible: Boolean = true,
    var keySigVisible: Boolean = true,
    var cueLineVisible: Boolean = true,
    var currentBeatVisible: Boolean = true,
    var soundOn: Boolean = true,
) {
    val change = MutableStateFlow(Unit)
    var tempo = tempo
        set(value) {
            field = value
//            change.value = Unit
        }

    val beatDuration: Long
        get() {
            val millisInAMinute = 60L * 1000
            return millisInAMinute / tempo
        }
    val barDuration: Long
        get() {
            return beatsPerChord * beatDuration
        }
}