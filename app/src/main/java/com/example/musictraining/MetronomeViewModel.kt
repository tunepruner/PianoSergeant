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
class MetronomeViewModel(
    private val settingsRepository: SettingsRepository,
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

    private var barJob: Job? = null
    var metronomeJobs: MutableSet<Job>? = HashSet()

    private val _newBar: MutableLiveData<Unit> = MutableLiveData(Unit)
    val newBar: LiveData<Unit> = _newBar

    init {
        viewModelScope.launch {
            settingsRepository.current.collect {
                _currentSettings.value = it
            }
        }
    }

    fun updateLevelReading(indicator: TextView, selector: SeekBar, max: Int, min: Int) {
        indicator.text = ((((max - min).toDouble() / 100) * selector.progress) + min).roundToInt().toString()
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
        barJob = viewModelScope.launch {
            startNewBar()
        }
    }

    private suspend fun startNewBar() {
        metronomeJobs?.addAll(
            setOf(
                viewModelScope.launch {
                    incrementBeat()
                },
                viewModelScope.launch {
                    _currentSettings.value?.let {
                        val barStartTime = Date()
                        while (true) {
                            moveBarForward(
                                barStartTime,
                            )
                        }
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

    private suspend fun incrementBeat() {
        _beatLiveData.value = (_beatLiveData.value ?: 0) + 1

        delay(settingsRepository.current.value.beatDuration)

        if (_beatLiveData.value == settingsRepository.current.value.beatsPerChord) {
            endBar()
            startNewBar()
        } else {
            incrementBeat()
        }
    }

    private fun endBar() {
        _beatLiveData.value = 0
        barJob?.cancel()
        metronomeJobs?.forEach { it.cancel() }
    }

    private suspend fun moveBarForward(timeStarted: Date) {
        delay(10)
        val barDuration: Long =
            _currentSettings.value?.barDuration
                ?: settingsRepository.current.value.barDurationFromTempo(MIN_TEMPO)
        Log.i("12345", "bar duration = ${_currentSettings.value?.barDuration}")
        //TODO somewhere around here, weird progress bar problem
        val preCalc = ((Date().time - timeStarted.time).toDouble() / barDuration) * 100
        if (preCalc < 100) {
            _barPercentage.value = preCalc.toInt()
        } else {
            _barPercentage.value = 0
        }
    }

    fun updateSettings() {
    }

    fun forceStop() {
        onStopButtonPressed()
    }
}
