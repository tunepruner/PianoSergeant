package com.example.musictraining

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.roundToInt

class Settings(
    var tempo: Int = 120,
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
    val beatDuration: Long
        get() {
            return beatDurationFromTempo(tempo)
        }
    val barDuration: Long
        get() {
            return beatsPerChord * beatDurationFromTempo(tempo)
        }

    fun beatDurationFromTempo(tempo: Int): Long {
        val millisInAMinute = 60L * 1000
        return millisInAMinute / tempo
    }

    fun barDurationFromTempo(tempo: Int): Long {
        return beatDurationFromTempo(tempo) * beatsPerChord
    }

    fun setTempoFromPercentage(percentage: Int) {
        tempo = calculateLevelFromPercentage(percentage, MAX_TEMPO, MIN_TEMPO)
    }

    fun setBeatsPerChordFromPercentage(percentage: Int) {
        beatsPerChord = calculateLevelFromPercentage(percentage, MAX_BEATS_PER_CHORD, MIN_BEATS_PER_CHORD)
    }

    fun setChordDistanceFromPercentage(percentage: Int) {
        chordDistance = calculateLevelFromPercentage(percentage, MAX_DISTANCE, MIN_DISTANCE)
    }
}