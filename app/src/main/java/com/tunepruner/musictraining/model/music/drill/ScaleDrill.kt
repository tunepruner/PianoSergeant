package com.tunepruner.musictraining.model.music.drill

import com.google.gson.annotations.SerializedName
import com.tunepruner.musictraining.model.music.drill.items.Interval
import com.tunepruner.musictraining.model.music.drill.items.IntervalRequirements

class ScaleDrill(
    id: String,
    var notesPerBeat: Int = 2,
    var intervalRequirements: IntervalRequirements = IntervalRequirements.NONE,
    @SerializedName("interval_less_than_value")
    var intervalLessThanValue: Interval = Interval.PERFECT_FIFTH,
    @SerializedName("interval_greater_than_value")
    var intervalGreaterThanValue: Interval = Interval.MAJOR_SECOND,
) : Drill(id = id)