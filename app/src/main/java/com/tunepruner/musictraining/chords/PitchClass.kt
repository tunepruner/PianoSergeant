package com.tunepruner.musictraining.chords

sealed class PitchClass(
    val lowestPitch: Pitch,
    val enharmonics: List<PitchClass>? = null,
    val signBias: Pitch.SignBias? = null,
    val name: String? = null
) {
    object Aflat : PitchClass(
        lowestPitch = Pitch(8),
        enharmonics = listOf(Gsharp),
        signBias = Pitch.SignBias.FLAT,
        name = "Ab",
    )

    object A : PitchClass(
        lowestPitch = Pitch(9),
        name = "A",
    )

    object Asharp : PitchClass(
        lowestPitch = Pitch(10),
        enharmonics = listOf(Bflat),
        signBias = Pitch.SignBias.SHARP,
        name = "A#",
    )

    object Bflat : PitchClass(
        lowestPitch = Pitch(10),
        enharmonics = listOf(Asharp),
        signBias = Pitch.SignBias.FLAT,
        name = "Bb",
    )

    object B : PitchClass(
        lowestPitch = Pitch(11),
        name = "B",
    )

    object C : PitchClass(
        lowestPitch = Pitch(0),
        name = "C",
    )

    object Csharp : PitchClass(
        lowestPitch = Pitch(1),
        enharmonics = listOf(Dflat),
        signBias = Pitch.SignBias.SHARP,
        name = "C#",
    )

    object Dflat : PitchClass(
        lowestPitch = Pitch(1),
        enharmonics = listOf(Csharp),
        signBias = Pitch.SignBias.FLAT,
        name = "Db",
    )

    object D : PitchClass(
        lowestPitch = Pitch(2),
        name = "D",
    )

    object Dsharp : PitchClass(
        lowestPitch = Pitch(3),
        enharmonics = listOf(Eflat),
        signBias = Pitch.SignBias.SHARP,
        name = "D#",
    )

    object Eflat : PitchClass(
        lowestPitch = Pitch(3),
        enharmonics = listOf(Dsharp),
        signBias = Pitch.SignBias.FLAT,
        name = "Eb",
    )

    object E : PitchClass(
        lowestPitch = Pitch(4),
        name = "E",
    )

    object F : PitchClass(
        lowestPitch = Pitch(5),
        name = "F",
    )

    object Fsharp : PitchClass(
        lowestPitch = Pitch(6),
        enharmonics = listOf(Gflat),
        signBias = Pitch.SignBias.SHARP,
        name = "F#",
    )

    object Gflat : PitchClass(
        lowestPitch = Pitch(6),
        enharmonics = listOf(Fsharp),
        signBias = Pitch.SignBias.FLAT,
        name = "Gb",
    )

    object G : PitchClass(
        lowestPitch = Pitch(7),
        name = "G",
    )

    object Gsharp : PitchClass(
        lowestPitch = Pitch(8),
        enharmonics = listOf(Aflat),
        signBias = Pitch.SignBias.SHARP,
        name = "G#",
    )

    companion object {
        private val all = listOf(
            Aflat,
            A,
            Asharp,
            Bflat,
            B,
            C,
            Csharp,
            Dflat,
            D,
            Dsharp,
            Eflat,
            E,
            F,
            Fsharp,
            Gflat,
            G,
            Gsharp
        )

        fun getPitchClass(pitch: Pitch): PitchClass {
            all.first {
                it.isMember(pitch)
            }.apply {
                return this
            }
        }
    }

    fun isMember(pitch: Pitch): Boolean {
        val isSameTone = if (
            pitch.value == 0
        ) {
            this is C
        }else {
            pitch.value % 12 == lowestPitch.value
        }
        pitch.signBias?.let {
            return it == this.signBias && isSameTone
        }
        return isSameTone
    }
}

