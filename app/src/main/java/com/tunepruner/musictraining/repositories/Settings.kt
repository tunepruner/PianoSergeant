package com.tunepruner.musictraining.repositories

import com.tunepruner.musictraining.calculateLevelFromPercentage
import com.tunepruner.musictraining.ui.MAX_BEATS_PER_CHORD
import com.tunepruner.musictraining.ui.MAX_DISTANCE
import com.tunepruner.musictraining.ui.MAX_TEMPO
import com.tunepruner.musictraining.ui.MIN_BEATS_PER_CHORD
import com.tunepruner.musictraining.ui.MIN_DISTANCE
import com.tunepruner.musictraining.ui.MIN_TEMPO
import kotlinx.coroutines.flow.MutableStateFlow

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
    var timeConstraint: TimeConstraint = TimeConstraint.Metronome,
    var mode: Mode = Mode.ChordMode,
    var notesPerBeat: Int = 2,
    var intervalRequirements: IntervalRequirements = IntervalRequirements.None,
    var inversions: List<Inversion> = listOf(
        Inversion.RootPosition,
        Inversion.FirstInversion,
        Inversion.SecondInversion,
        Inversion.ThirdInversion,
    ),
    var chordQualities: List<ChordQuality> = listOf(
        ChordQuality.MajorTriad,
        ChordQuality.MinorTriad,
        ChordQuality.DiminishedTriad,
        ChordQuality.AugmentedTriad,
        ChordQuality.Sus2Triad,
        ChordQuality.Sus4Triad,
        ChordQuality.DominantSeventh,
        ChordQuality.MajorSeventh,
        ChordQuality.MinorSeventh,
        ChordQuality.MinorMajorSeventh,
        ChordQuality.HalfDiminishedSeventh,
        ChordQuality.FullDiminishedSeventh,
        ChordQuality.AugmentedSeventh,
        ChordQuality.AugmentedMajorSeventh,
        ChordQuality.DominantSeventhSus4,
    ),
    var noteDoublingRequirement: NoteDoublingRequirement = NoteDoublingRequirement.None,
    var spacingRequirement: SpacingRequirement = SpacingRequirement.None,
    var registerRequirement: RegisterRequirement = RegisterRequirement.None,
    var keys: List<Key> = listOf(
        Key.A,
        Key.Bb,
        Key.B,
        Key.C,
        Key.Db,
        Key.D,
        Key.Eb,
        Key.E,
        Key.F,
        Key.Fsharp,
        Key.G,
        Key.Ab,
    ),
    var algorithmForPrompts: AlgorithmSetting = AlgorithmSetting.Random,
    var randomSubSetting: RandomSubSetting = RandomSubSetting.TwelveToneRow,
    var patternSubSetting: PatternSubSetting = PatternSubSetting.Chromatic
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
        beatsPerChord =
            calculateLevelFromPercentage(percentage, MAX_BEATS_PER_CHORD, MIN_BEATS_PER_CHORD)
    }

    fun setChordDistanceFromPercentage(percentage: Int) {
        chordDistance = calculateLevelFromPercentage(percentage, MAX_DISTANCE, MIN_DISTANCE)
    }

}

sealed class PatternSubSetting {
    object Chromatic : PatternSubSetting()
    object InFifths : PatternSubSetting()
    object InFourths : PatternSubSetting()
}

sealed class RandomSubSetting {
    object TwelveToneRow : RandomSubSetting()
    object PreferMostDifficult : RandomSubSetting()
}

sealed class AlgorithmSetting {
    object Random : AlgorithmSetting()
    object Pattern : AlgorithmSetting()
}

sealed class Key {
    object A : Key()
    object Bb : Key()
    object B : Key()
    object C : Key()
    object Db : Key()
    object D : Key()
    object Eb : Key()
    object E : Key()
    object F : Key()
    object Fsharp : Key()
    object G : Key()
    object Ab : Key()
}

sealed class RegisterRequirement {
    object None : RegisterRequirement()
    object RequireVoiceLeading : RegisterRequirement()
    object RequireCommonTopNote : RegisterRequirement()
    object RequireCommonBottomNote : RegisterRequirement()
    object RequireLeapGreaterThan5th : RegisterRequirement()
}

sealed class SpacingRequirement {
    object None : SpacingRequirement()
    object ClosedVoicing : SpacingRequirement()
    object OpenVoicing : SpacingRequirement()
}

sealed class NoteDoublingRequirement {
    object None : NoteDoublingRequirement()
    object SpecificAmount : NoteDoublingRequirement()
}

sealed class ChordQuality {
    object MajorTriad : ChordQuality()
    object MinorTriad : ChordQuality()
    object DiminishedTriad : ChordQuality()
    object AugmentedTriad : ChordQuality()
    object Sus2Triad : ChordQuality()
    object Sus4Triad : ChordQuality()
    object DominantSeventh : ChordQuality()
    object MajorSeventh : ChordQuality()
    object MinorSeventh : ChordQuality()
    object MinorMajorSeventh : ChordQuality()
    object HalfDiminishedSeventh : ChordQuality()
    object FullDiminishedSeventh : ChordQuality()
    object AugmentedSeventh : ChordQuality()
    object AugmentedMajorSeventh : ChordQuality()
    object DominantSeventhSus4 : ChordQuality()
}

sealed class Inversion {
    object RootPosition : Inversion()
    object FirstInversion : Inversion()
    object SecondInversion : Inversion()
    object ThirdInversion : Inversion()
}

sealed class IntervalRequirements {
    object None : IntervalRequirements()
    object LessThan : IntervalRequirements()
    object GreaterThan : IntervalRequirements()
}

sealed class Mode {
    object ChordMode : Mode()
    object ScaleMode : Mode()
}

sealed class TimeConstraint {
    object Metronome : TimeConstraint()
    object RapidFire : TimeConstraint()
}
