package com.tunepruner.musictraining.chords


data class Pitch(val value: Int, val signBias: SignBias? = null) {
    enum class SignBias { SHARP, FLAT }

    val pitchClass: PitchClass
        get() {
            return PitchClass.getPitchClass(this)
        }

    override fun equals(other: Any?): Boolean {
        return other is Pitch &&
                return value == other.value
    }

}