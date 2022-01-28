package com.tunepruner.musictraining.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tunepruner.musictraining.repositories.Mode
import com.tunepruner.musictraining.repositories.SettingsRepository
import com.tunepruner.musictraining.repositories.TimeConstraint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingsViewModel(private val settingsRepo: SettingsRepository) : ViewModel() {
    val settings = settingsRepo.current.value

    private var _mode = MutableLiveData<Mode>()
    var mode: LiveData<Mode> = _mode

    private var _timeConstraint = MutableLiveData<TimeConstraint>()
    var timeConstraint: LiveData<TimeConstraint> = _timeConstraint

    init {
        _mode.value = settings.mode
        _timeConstraint.value = settings.timeConstraint
    }

    fun enableChordMode() {
        updateSettings {
            settings.mode = Mode.CHORD
            _mode.value = Mode.CHORD
        }
    }

    fun enableScaleMode() {
        updateSettings {
            settings.mode = Mode.SCALE
            _mode.value = Mode.SCALE
        }
    }

    fun enableMetronome() {
        updateSettings {
            settings.timeConstraint = TimeConstraint.METRONOME
            _timeConstraint.value = TimeConstraint.METRONOME
        }
    }

    fun enableRapidFire() {
        updateSettings {
            settings.timeConstraint = TimeConstraint.RAPID_FIRE
            _timeConstraint.value = TimeConstraint.RAPID_FIRE
        }
    }

    private fun updateSettings(action: () -> Unit) {
        action.invoke()
        settingsRepo.persist()
    }
}

