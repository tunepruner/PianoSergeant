package com.tunepruner.musictraining.model.music.drill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tunepruner.musictraining.calculateLevelFromPercentage
import com.tunepruner.musictraining.model.music.drill.items.AlgorithmSetting
import com.tunepruner.musictraining.model.music.drill.items.Key
import com.tunepruner.musictraining.model.music.drill.items.Mode
import com.tunepruner.musictraining.model.music.drill.items.PatternSubSetting
import com.tunepruner.musictraining.model.music.drill.items.TimeConstraint
import com.tunepruner.musictraining.ui.MAX_BEATS_PER_CHORD
import com.tunepruner.musictraining.ui.MAX_DISTANCE
import com.tunepruner.musictraining.ui.MAX_TEMPO
import com.tunepruner.musictraining.ui.MIN_BEATS_PER_CHORD
import com.tunepruner.musictraining.ui.MIN_DISTANCE
import com.tunepruner.musictraining.ui.MIN_TEMPO

@Entity(tableName = "drills")
class Drill(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "chord_drill") var chordDrill: ChordDrill? = null,
    @ColumnInfo(name = "scale_drill") var scaleDrill: ScaleDrill? = null,

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
){
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