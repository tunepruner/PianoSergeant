package com.tunepruner.musictraining.model.music.drill//package com.tunepruner.musictraining.repositories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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

@Entity
data class ChordDrill(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "tempo") var tempo: Int = 120,
    @ColumnInfo(name = "chord_distance") var chordDistance: Int = 3,
    @ColumnInfo(name = "beats_per_chord") var beatsPerChord: Int = 4,
    @ColumnInfo(name = "chord_visible") var chordVisible: Boolean = true,
    @ColumnInfo(name = "mode_visible") var modeVisible: Boolean = true,
    @ColumnInfo(name = "key_sig_visible") var keySigVisible: Boolean = true,
    @ColumnInfo(name = "cue_line_visible") var cueLineVisible: Boolean = true,
    @ColumnInfo(name = "current_beat_visible") var currentBeatVisible: Boolean = true,
    @ColumnInfo(name = "sound_on") var soundOn: Boolean = true,
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
//
//const val MAX_DOUBLING_AMOUNT = 4
//const val MIN_DOUBLING_AMOUNT = 1
//const val MAX_NOTES_PER_BEAT = 4
//const val MIN_NOTES_PER_BEAT = 1
//
//enum class Interval(val uiName: String){
//    MINOR_SECOND("minor 2nd"),
//    MAJOR_SECOND("major 2nd"),
//    MINOR_THIRD("minor 3rd"),
//    MAJOR_THIRD("major 3rd"),
//    PERFECT_FOURTH("perfect 4th"),
//    TRITONE("tritone"),
//    PERFECT_FIFTH("perfect 5th"),
//    MINOR_SIXTH("minor 6th"),
//    MAJOR_SIXTH("major 6th"),
//    MINOR_SEVENTH("minor 7th"),
//    MAJOR_SEVENTH("major 7th"),
//}
//
//val allIntervals: List<Interval> = listOf(
//    Interval.MINOR_SECOND,
//    Interval.MAJOR_SECOND,
//    Interval.MINOR_THIRD,
//    Interval.MAJOR_THIRD,
//    Interval.PERFECT_FOURTH,
//    Interval.TRITONE,
//    Interval.PERFECT_FIFTH,
//    Interval.MINOR_SIXTH,
//    Interval.MAJOR_SIXTH,
//    Interval.MINOR_SEVENTH,
//    Interval.MAJOR_SEVENTH,
//)
//
//enum class PatternSubSetting {
//    CHROMATIC,
//    IN_FIFTHS,
//    IN_FOURTHS,
//}
//
//enum class AlgorithmSetting {
//    RANDOM,
//    PATTERN
//}
//
//enum class Key {
//    A_MAJOR,
//    Bb_MAJOR,
//    B_MAJOR,
//    C_MAJOR,
//    Db_MAJOR,
//    D_MAJOR,
//    Eb_MAJOR,
//    E_MAJOR,
//    F_MAJOR,
//    Fsharp_MAJOR,
//    G_MAJOR,
//    Ab_MAJOR,
//    A_MINOR,
//    Bb_MINOR,
//    B_MINOR,
//    C_MINOR,
//    Db_MINOR,
//    D_MINOR,
//    Eb_MINOR,
//    E_MINOR,
//    F_MINOR,
//    Fsharp_MINOR,
//    G_MINOR,
//    Ab_MINOR,
//}
//
//enum class RegisterRequirement {
//    NONE,
//    REQUIRE_VOICE_LEADING,
//    REQUIRE_COMMON_TOP_NOTE,
//    REQUIRE_COMMON_BOTTOM_NOTE,
//    REQUIRE_LEAP_GREATER_THAN_5TH,
//}
//
//enum class SpacingRequirement {
//    NONE,
//    CLOSED_VOICING,
//    OPEN_VOICING,
//}
//
//enum class NoteDoublingRequirement {
//    NONE,
//    SPECIFIC_AMOUNT,
//}
//
//enum class ChordQuality {
//    MAJOR_TRIAD,
//    MINOR_TRIAD,
//    DIMINISHED_TRIAD,
//    AUGMENTED_TRIAD,
//    SUS_2_TRIAD,
//    SUS_4_TRIAD,
//    DOMINANT_SEVENTH,
//    MAJOR_SEVENTH,
//    MINOR_SEVENTH,
//    MINOR_MAJOR_SEVENTH,
//    HALF_DIMINISHED_SEVENTH,
//    FULL_DIMINISHED_SEVENTH,
//    AUGMENTED_SEVENTH,
//    AUGMENTED_MAJOR_SEVENTH,
//    DOMINANT_SEVENTH_SUS_4,
//}
//
//enum class Inversion {
//    ROOT_POSITION,
//    FIRST_INVERSION,
//    SECOND_INVERSION,
//    THIRD_INVERSION,
//}
//
//enum class IntervalRequirements {
//    NONE,
//    LESS_THAN,
//    GREATER_THAN,
//}
//
//enum class Mode {
//    CHORD,
//    SCALE
//}
//
//enum class TimeConstraint {
//    METRONOME,
//    RAPID_FIRE
//}
