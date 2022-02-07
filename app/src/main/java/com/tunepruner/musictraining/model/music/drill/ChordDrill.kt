package com.tunepruner.musictraining.model.music.drill//package com.tunepruner.musictraining.repositories

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.tunepruner.musictraining.model.music.drill.items.ChordQuality
import com.tunepruner.musictraining.model.music.drill.items.Interval
import com.tunepruner.musictraining.model.music.drill.items.IntervalRequirements
import com.tunepruner.musictraining.model.music.drill.items.Inversion
import com.tunepruner.musictraining.model.music.drill.items.NoteDoublingRequirement
import com.tunepruner.musictraining.model.music.drill.items.RegisterRequirement
import com.tunepruner.musictraining.model.music.drill.items.SpacingRequirement

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