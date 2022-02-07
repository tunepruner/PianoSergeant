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

class ScaleDrill(
    id: String,
    var notesPerBeat: Int = 2,
    var intervalRequirements: IntervalRequirements = IntervalRequirements.NONE,
    @SerializedName("interval_less_than_value")
    var intervalLessThanValue: Interval = Interval.PERFECT_FIFTH,
    @SerializedName("interval_greater_than_value")
    var intervalGreaterThanValue: Interval = Interval.MAJOR_SECOND,
) : Drill(id = id)