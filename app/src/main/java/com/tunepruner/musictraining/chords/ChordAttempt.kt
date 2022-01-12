package com.tunepruner.musictraining.chords

import java.util.*

const val THREASHHOLD_FOR_ATTEMPT_DOOR = 500

class ChordAttempt(pitches: List<Pitch>) {
    val startedAt = Date().time
    var doorClosed: Boolean = false
        get() = Date().time - startedAt > THREASHHOLD_FOR_ATTEMPT_DOOR

    val pitchClasses = pitches.map {
        it.pitchClass
    }.toMutableSet()

    init {
        //Just a sample of how to use this
        val newPitch = Pitch(4)
        with(ChordAttempt(listOf())) {
            if (!this.doorClosed) {
                pitchClasses.add(newPitch.pitchClass)
            } else {
                ChordAttempt(listOf(newPitch))
            }
        }
    }
}