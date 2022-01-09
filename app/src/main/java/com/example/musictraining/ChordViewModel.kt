package com.example.musictraining

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

typealias RelationOptions = Pair<Int, Int>
typealias Spellings = Pair<String, String>

class ChordViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _currentChord: MutableLiveData<String?> = MutableLiveData(null)
    val currentChord: LiveData<String?> = _currentChord

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
        }
        return climbedTo
    }

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