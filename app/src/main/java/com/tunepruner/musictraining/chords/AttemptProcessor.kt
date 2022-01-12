package com.tunepruner.musictraining.chords

import java.util.*

class AttemptProcessor {
    private val attempts: Stack<ChordAttempt> = Stack()
    var attemptsCount = 0
    var successess = 0
    var failures = 0
        get() = attemptsCount - successess

    fun insertNewData(newData: Int) {
        val newPitch = Pitch(newData)
        with(attempts) {
            if (peek().doorClosed) {
                push(
                    ChordAttempt(
                        listOf(newPitch)
                    )
                )
            } else {
                peek().pitchClasses.add(
                    newPitch.pitchClass
                )
            }
        }
    }
}