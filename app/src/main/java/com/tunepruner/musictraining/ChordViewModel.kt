package com.tunepruner.musictraining

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

typealias RelationOptions = Pair<Int, Int>
typealias Spellings = Pair<String, String>

class ChordViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _currentChord: MutableLiveData<String?> = MutableLiveData(null)
    val currentChord: LiveData<String?> = _currentChord
//    val stack: Stack<String> = Stack<String>().apply { add("C") }

    val pitches = listOf(
        "A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "F#", "G", "Ab"
    )

    val keys = listOf(
        "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#"
    )


    private fun getChord(current: String, desiredDistance: Int): String {
        var climbedTo = current
        for (x in 1..desiredDistance) {
            climbedTo = climbOneLevel(climbedTo)
            Log.i("12345", "$climbedTo")
        }
        return climbedTo
//        return if (isNotRepetition(climbedTo)) {
//            climbedTo.apply { stack.push(climbedTo) }
//        } else {
//            keys[(0..keys.size).random()]
//        }
    }

//    private fun isNotRepetition(checkForRepetition: String): Boolean {
//        return checkForRepetition != stack.lastElement() &&
//                checkForRepetition != stack.secondToLastElement()
//    }

    private fun climbOneLevel(climbFrom: String): String {
        return when {
            climbFrom.contains("C#") -> {
                climbOneLevel("Db")
            }
            climbFrom.contains("Cb") -> {
                climbOneLevel("B")
            }
            else -> {
                keys[keys.indexOf(climbFrom) + 1]
            }
        }
    }

    fun triggerNextChord() {
        _currentChord.value = getChord(
            _currentChord.value ?: "C",
            settingsRepository.current.value.chordDistance)
    }

}