package com.tunepruner.musictraining.model.music


sealed class Interval(val halfStepsUp: Int) {
    object MinorSecond : Interval(1)
    object MajorSecond : Interval(2)
    object MinorThird : Interval(3)
    object MajorThird : Interval(4)
    object PerfectFourth : Interval(5)
    object Tritone : Interval(6)
    object PerfectFifth : Interval(7)
    object MinorSixth : Interval(8)
    object MajorSixth : Interval(9)
    object MinorSeventh : Interval(10)
    object MajorSeventh : Interval(11)

    private val all = listOf(
        MinorSecond,
        MajorSecond,
        MinorThird,
        MajorThird,
        PerfectFourth,
        PerfectFifth,
        MinorSixth,
        MajorSixth,
        MinorSeventh,
        MajorSeventh
    )

    override fun equals(other: Any?): Boolean {
        return other is Interval &&
                other.halfStepsUp == this.halfStepsUp
    }

    fun getInterval(lower: Int, higher: Int): Interval {
        all.first {
            it.halfStepsUp == higher - lower
        }.apply {
            return this
        }
    }
}