package com.example.musictraining

import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
class FirstViewModel(
    private val settingsRepository: SettingsRepository,
//    private val chordViewModel: ChordViewModel
) : ViewModel() {

    private var _currentSettings: MutableLiveData<Settings> =
        MutableLiveData<Settings>(settingsRepository.current.value)
    var currentSettings: LiveData<Settings> = _currentSettings

    private val _playState: MutableLiveData<PlayState> =
        MutableLiveData<PlayState>(PlayState.STOPPED)
    val playState: LiveData<PlayState> = _playState

    private val _beatLiveData: MutableLiveData<Int> = MutableLiveData(0)
    val beatLiveData: LiveData<Int> = _beatLiveData

    private val _barPercentage: MutableLiveData<Int> = MutableLiveData(0)
    val barPercentage: LiveData<Int> = _barPercentage


    var metronomeJobs: MutableSet<Job>? = HashSet()

    init {
        viewModelScope.launch {
            settingsRepository.current.collect {
                _currentSettings.value = it
            }
        }
    }


    fun onChordCheckBoxClicked() {
        TODO("Not yet implemented")
    }

    fun onModeCheckBoxClicked() {
        TODO("Not yet implemented")
    }

    fun onKeySigCheckBoxClicked() {
        TODO("Not yet implemented")
    }

    fun onCueLineCheckBoxClicked() {
        TODO("Not yet implemented")
    }

    fun onMetronomeCheckBoxClicked() {
        TODO("Not yet implemented")
    }

    fun onSoundCheckBoxClicked() {
        TODO("Not yet implemented")
    }


    fun updateLevelReading(indicator: TextView, selector: SeekBar, max: Int) {
        indicator.text = ((max.toDouble() / 100) * selector.progress).roundToInt().toString()
    }

    fun onPlayStopButtonPressed() {
        if (_playState.value == PlayState.PLAYING) {
            onStopButtonPressed()
        } else {
            onPlayButtonPressed()
        }
    }

    private fun onPlayButtonPressed() {
        _playState.value = PlayState.PLAYING
        metronomeJobs?.addAll(
            setOf(
                viewModelScope.launch {
                    moveBeatsForward()
                },
                viewModelScope.launch {
                    _currentSettings.value?.let {
                        moveBarForward(
                            Date(),
                            it.beatDuration * it.beatsPerChord
                        )
                    }
                })
        )
    }

    private fun onStopButtonPressed() {
        _beatLiveData.value = 0
        _barPercentage.value = 0
        _playState.value = PlayState.STOPPED
        metronomeJobs?.forEach {
            it.cancel()
        }
    }

    private suspend fun moveBeatsForward() {
        if (_beatLiveData.value == settingsRepository.current.value.beatsPerChord) {
            _beatLiveData.value = 1
        } else {
            _beatLiveData.value = (_beatLiveData.value ?: 0) + 1
        }

        delay(settingsRepository.current.value.beatDuration)

        moveBeatsForward()
    }

    private suspend fun moveBarForward(timeStarted: Date, barDuration: Long) {
        delay(10)
        val preCalc = ((Date().time - timeStarted.time).toDouble() / barDuration) * 100
        if (preCalc < 100) {
            _barPercentage.value = preCalc.toInt()
            moveBarForward(timeStarted, barDuration)
        }else{
            _barPercentage.value = 0
            moveBarForward(Date(), barDuration)
        }
    }

    fun updateSettings() {
    }

    fun forceStop() {
        onStopButtonPressed()
    }
}
