package com.tunepruner.musictraining

import android.R
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    private val clicker: MetronomeClicker,
) : ViewModel() {

    private var _currentSettings: MutableLiveData<Settings> =
        MutableLiveData<Settings>(settingsRepository.current.value)
    var currentSettings: LiveData<Settings> = _currentSettings

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
            settingsRepository.current.collect {
                _currentSettings.value = it
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
                    _currentSettings.value?.let {
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

        if (_currentSettings.value?.soundOn != false) {
            if (_publishedBeatNumber.value == 1) {
                clicker.playLoudSound()
            } else {
                clicker.playSoftSound()
            }
        }

        delay(settingsRepository.current.value.beatDuration)

        if (_publishedBeatNumber.value == settingsRepository.current.value.beatsPerChord) {
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
            _currentSettings.value?.barDuration
                ?: settingsRepository.current.value.barDurationFromTempo(MIN_TEMPO)
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

