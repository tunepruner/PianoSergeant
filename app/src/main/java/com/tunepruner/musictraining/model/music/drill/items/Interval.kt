package com.tunepruner.musictraining.model.music.drill.items

enum class Interval(val uiName: String){
    MINOR_SECOND("minor 2nd"),
    MAJOR_SECOND("major 2nd"),
    MINOR_THIRD("minor 3rd"),
    MAJOR_THIRD("major 3rd"),
    PERFECT_FOURTH("perfect 4th"),
    TRITONE("tritone"),
    PERFECT_FIFTH("perfect 5th"),
    MINOR_SIXTH("minor 6th"),
    MAJOR_SIXTH("major 6th"),
    MINOR_SEVENTH("minor 7th"),
    MAJOR_SEVENTH("major 7th"),
}

val allIntervals: List<Interval> = listOf(
    Interval.MINOR_SECOND,
    Interval.MAJOR_SECOND,
    Interval.MINOR_THIRD,
    Interval.MAJOR_THIRD,
    Interval.PERFECT_FOURTH,
    Interval.TRITONE,
    Interval.PERFECT_FIFTH,
    Interval.MINOR_SIXTH,
    Interval.MAJOR_SIXTH,
    Interval.MINOR_SEVENTH,
    Interval.MAJOR_SEVENTH,
)