package com.tunepruner.musictraining.viewmodel

import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunepruner.musictraining.model.PlayState
import com.tunepruner.musictraining.model.music.drill.Drill
import com.tunepruner.musictraining.repositories.DrillSettingsRepository
import com.tunepruner.musictraining.ui.MIN_TEMPO
import com.tunepruner.musictraining.util.MetronomeClicker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt


@ExperimentalCoroutinesApi
class MetronomeViewModel(
    private val drillSettingsRepository: DrillSettingsRepository,
    private val clicker: MetronomeClicker,
) : ViewModel() {

    private var _currentChordDrill: MutableLiveData<Drill> =
        MutableLiveData<Drill>(drillSettingsRepository.current.value)
    var currentChordDrill: LiveData<Drill> = _currentChordDrill

    private val _playState: MutableLiveData<PlayState> =
        MutableLiveData<PlayState>(PlayState.STOPPED)
    val playState: LiveData<PlayState> = _playState

    private val _publishedBeatNumber: MutableLiveData<Int> = MutableLiveData(0)
    val publishedBeatNumber: LiveData<Int> = _publishedBeatNumber

    private val _barPercentage: MutableLiveData<Int> = MutableLiveData(0)
    val barPercentage: LiveData<Int> = _barPercentage

    private var barJob: Job? = null
    var metronomeJobs: MutableSet<Job>? = HashSet()

    init {
        viewModelScope.launch {
            drillSettingsRepository.current.collect {
                _currentChordDrill.value = it
            }
        }
    }

    fun updateLevelReading(indicator: TextView, selector: SeekBar, max: Int, min: Int) {
        indicator.text =
            ((((max - min).toDouble() / 100) * selector.progress) + min).roundToInt().toString()
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
        val barStartTime = Date()
        metronomeJobs?.addAll(
            setOf(
                viewModelScope.launch {
                    incrementBeat(barStartTime)
                },
                viewModelScope.launch {
                    _currentChordDrill.value?.let {
                        while (true) {
                            moveBarForward(barStartTime)
                        }
                    }
                })
        )
    }

    private fun onStopButtonPressed() {
        _publishedBeatNumber.value = 0
        _barPercentage.value = 0
        _playState.value = PlayState.STOPPED
        metronomeJobs?.forEach {
            it.cancel()
        }
    }

    private suspend fun incrementBeat(barStartTime: Date) {
        _publishedBeatNumber.value = (_publishedBeatNumber.value ?: 0) + 1

        if (_currentChordDrill.value?.soundOn != false) {
            if (_publishedBeatNumber.value == 1) {
                clicker.playLoudSound()
            } else {
                clicker.playSoftSound()
            }
        }

        delay(drillSettingsRepository.current.value.beatDuration)

        if (_publishedBeatNumber.value == drillSettingsRepository.current.value.beatsPerChord) {
            endBar()
            startNewBar()
        } else {
            incrementBeat(barStartTime)
        }
    }

    private fun endBar() {
        _publishedBeatNumber.value = 0
        barJob?.cancel()
        metronomeJobs?.forEach { it.cancel() }
    }

    private suspend fun moveBarForward(timeStarted: Date) {
        delay(10)
        val barDuration: Long =
            _currentChordDrill.value?.barDuration
                ?: drillSettingsRepository.current.value.barDurationFromTempo(MIN_TEMPO)
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

