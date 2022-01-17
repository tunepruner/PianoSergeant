package com.tunepruner.musictraining.model.music

sealed class ChordDefinition(
    val intervals: List<Interval>,
    val alsoWouldMatch: List<ChordDefinition>? = null
) {

    object MajorTriad : ChordDefinition(
        intervals = listOf(
            Interval.MajorThird,
            Interval.PerfectFifth,
        ),
        alsoWouldMatch = listOf(
            MajorSeventhChord,
            MajorSixthChord,
            DominantChord,
        )
    )

    object MinorTriad : ChordDefinition(
        intervals = listOf(
            Interval.MinorThird,
            Interval.PerfectFifth,
        ),
        alsoWouldMatch = listOf(
            MinorSeventhChord,
            MinorSixthChord,
            MinorMajorSeventhChord,
        )
    )

    object DiminishedTriad : ChordDefinition(
        intervals = listOf(
            Interval.MinorThird,
            Interval.Tritone,
        ),
        alsoWouldMatch = listOf(
            HalfDiminishedChord,
            FullDiminishedChord,
        )
    )

    object AugmentedTriad : ChordDefinition(
        intervals = listOf(
            Interval.MajorThird,
            Interval.MinorSixth,
        ),
        alsoWouldMatch = listOf(
            AugmentedMajorSeventhChord,
            AugmentedSeventhChord,
        )
    )

    object Sus2Triad : ChordDefinition(
        intervals = listOf(
            Interval.MajorSecond,
            Interval.PerfectFifth,
        )
    )

    object Sus4Triad : ChordDefinition(
        intervals = listOf(
            Interval.PerfectFourth,
            Interval.PerfectFifth,
        ),
        alsoWouldMatch = listOf(
            DominantSus4
        )
    )

    object MajorSeventhChord : ChordDefinition(
        intervals = listOf(
            Interval.MajorThird,
            Interval.PerfectFifth,
            Interval.MajorSeventh,
        ),
    )

    object MinorSeventhChord : ChordDefinition(
        listOf(
            Interval.MinorThird,
            Interval.PerfectFifth,
            Interval.MinorSeventh,
        )
    )

    object MajorSixthChord : ChordDefinition(
        listOf(
            Interval.MajorThird,
            Interval.PerfectFifth,
            Interval.MajorSixth,
        )
    )

    object MinorSixthChord : ChordDefinition(
        listOf(
            Interval.MinorThird,
            Interval.PerfectFifth,
            Interval.MajorSixth,
        )
    )

    object MinorMajorSeventhChord : ChordDefinition(
        listOf(
            Interval.MinorThird,
            Interval.PerfectFifth,
            Interval.MajorSeventh,
        )
    )

    object DominantChord : ChordDefinition(
        listOf(
            Interval.MajorThird,
            Interval.PerfectFifth,
            Interval.MinorSeventh,
        )
    )

    object AugmentedSeventhChord : ChordDefinition(
        listOf(
            Interval.MajorThird,
            Interval.MinorSixth,
            Interval.MinorSeventh,
        )
    )

    object AugmentedMajorSeventhChord : ChordDefinition(
        listOf(
            Interval.MajorThird,
            Interval.MinorSixth,
            Interval.MajorSeventh,
        )
    )

    object HalfDiminishedChord : ChordDefinition(
        listOf(
            Interval.MinorThird,
            Interval.Tritone,
            Interval.MinorSeventh,
        )
    )

    object FullDiminishedChord : ChordDefinition(
        listOf(
            Interval.MinorThird,
            Interval.Tritone,
            Interval.MajorSixth,
        )
    )

    object DominantSus4 : ChordDefinition(
        listOf(
            Interval.PerfectFourth,
            Interval.PerfectFifth,
            Interval.MinorSeventh,
        )
    )
}


