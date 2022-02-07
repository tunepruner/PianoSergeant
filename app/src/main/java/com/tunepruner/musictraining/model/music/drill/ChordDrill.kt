package com.tunepruner.musictraining.model.music.drill//package com.tunepruner.musictraining.repositories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
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
import java.lang.reflect.Type
import java.util.*

@Entity
class ChordDrill (
    id: String,
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
) : Drill(id = id)