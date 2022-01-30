package com.tunepruner.musictraining.model.music.drill

import com.google.gson.annotations.SerializedName
import com.tunepruner.musictraining.calculateLevelFromPercentage
import com.tunepruner.musictraining.model.music.drill.items.AlgorithmSetting
import com.tunepruner.musictraining.model.music.drill.items.ChordQuality
import com.tunepruner.musictraining.model.music.drill.items.Interval
import com.tunepruner.musictraining.model.music.drill.items.IntervalRequirements
import com.tunepruner.musictraining.model.music.drill.items.Inversion
import com.tunepruner.musictraining.model.music.drill.items.Key
import com.tunepruner.musictraining.model.music.drill.items.Mode
import com.tunepruner.musictraining.model.music.drill.items.NoteDoublingRequirement
import com.tunepruner.musictraining.model.music.drill.items.PatternSubSetting
import com.tunepruner.musictraining.model.music.drill.items.RegisterRequirement
import com.tunepruner.musictraining.model.music.drill.items.SpacingRequirement
import com.tunepruner.musictraining.model.music.drill.items.TimeConstraint
import com.tunepruner.musictraining.ui.MAX_BEATS_PER_CHORD
import com.tunepruner.musictraining.ui.MAX_DISTANCE
import com.tunepruner.musictraining.ui.MAX_TEMPO
import com.tunepruner.musictraining.ui.MIN_BEATS_PER_CHORD
import com.tunepruner.musictraining.ui.MIN_DISTANCE
import com.tunepruner.musictraining.ui.MIN_TEMPO

data class ChordDrill(
    var tempo: Int = 120,
    var chordDistance: Int = 3,
    var beatsPerChord: Int = 4,
    var chordVisible: Boolean = true,
    var modeVisible: Boolean = true,
    var keySigVisible: Boolean = true,
    var cueLineVisible: Boolean = true,
    var currentBeatVisible: Boolean = true,
    var soundOn: Boolean = true,
    @SerializedName("time_constraint")
    var timeConstraint: TimeConstraint = TimeConstraint.METRONOME,
    @SerializedName("mode")
    var mode: Mode = Mode.CHORD,
    var notesPerBeat: Int = 2,
    var intervalRequirements: IntervalRequirements = IntervalRequirements.NONE,
    @SerializedName("interval_less_than_value")
    var intervalLessThanValue: Interval = Interval.PERFECT_FIFTH,
    @SerializedName("interval_greater_than_value")
    var intervalGreaterThanValue: Interval = Interval.MAJOR_SECOND,
    @SerializedName("inversion")
    var inversions: MutableSet<Inversion> = mutableSetOf(
        Inversion.ROOT_POSITION,
        Inversion.FIRST_INVERSION,
        Inversion.SECOND_INVERSION,
        Inversion.THIRD_INVERSION,
    ),
    @SerializedName("chord_qualities")
    var chordQualities: MutableSet<ChordQuality> = mutableSetOf(
        ChordQuality.MAJOR_TRIAD,
        ChordQuality.MINOR_TRIAD,
        ChordQuality.DIMINISHED_TRIAD,
        ChordQuality.AUGMENTED_TRIAD,
        ChordQuality.SUS_2_TRIAD,
        ChordQuality.SUS_4_TRIAD,
        ChordQuality.MAJOR_SEVENTH,
        ChordQuality.DOMINANT_SEVENTH,
        ChordQuality.MINOR_SEVENTH,
        ChordQuality.MINOR_MAJOR_SEVENTH,
        ChordQuality.HALF_DIMINISHED_SEVENTH,
        ChordQuality.FULL_DIMINISHED_SEVENTH,
        ChordQuality.AUGMENTED_SEVENTH,
        ChordQuality.AUGMENTED_MAJOR_SEVENTH,
        ChordQuality.DOMINANT_SEVENTH_SUS_4,
    ),
    @SerializedName("note_doubling_requirement")
    var noteDoublingRequirement: NoteDoublingRequirement = NoteDoublingRequirement.NONE,
    @SerializedName("note_doubling_amount")
    var noteDoublingAmount: Int = 0,
    var spacingRequirement: SpacingRequirement = SpacingRequirement.NONE,
    var registerRequirement: RegisterRequirement = RegisterRequirement.NONE,
    var keys: MutableSet<Key> = mutableSetOf(
        Key.A_MAJOR,
        Key.Bb_MAJOR,
        Key.B_MAJOR,
        Key.C_MAJOR,
        Key.Db_MAJOR,
        Key.D_MAJOR,
        Key.Eb_MAJOR,
        Key.E_MAJOR,
        Key.F_MAJOR,
        Key.Fsharp_MAJOR,
        Key.G_MAJOR,
        Key.Ab_MAJOR,
        Key.A_MINOR,
        Key.Bb_MINOR,
        Key.B_MINOR,
        Key.C_MINOR,
        Key.Db_MINOR,
        Key.D_MINOR,
        Key.Eb_MINOR,
        Key.E_MINOR,
        Key.F_MINOR,
        Key.Fsharp_MINOR,
        Key.G_MINOR,
        Key.Ab_MINOR,
    ),
    var algorithmForPrompts: AlgorithmSetting = AlgorithmSetting.RANDOM,
    var patternSubSetting: PatternSubSetting = PatternSubSetting.CHROMATIC,
) {
    //    val change = MutableStateFlow(Unit)
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
        tempo = percentage.calculateLevelFromPercentage(MAX_TEMPO, MIN_TEMPO)
    }

    fun setBeatsPerChordFromPercentage(percentage: Int) {
        beatsPerChord =
            percentage.calculateLevelFromPercentage(MAX_BEATS_PER_CHORD, MIN_BEATS_PER_CHORD)
    }

    fun setChordDistanceFromPercentage(percentage: Int) {
        chordDistance = percentage.calculateLevelFromPercentage(MAX_DISTANCE, MIN_DISTANCE)
    }
}


