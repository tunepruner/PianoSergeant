package com.tunepruner.musictraining.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tunepruner.musictraining.repositories.Mode
import com.tunepruner.musictraining.repositories.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingsViewModel(private val settingsRepo: SettingsRepository) : ViewModel() {
    val settings = settingsRepo.current.value
    private var _mode = MutableLiveData<Mode>()
    var mode: LiveData<Mode> = _mode

    init {
        _mode.value = settings.mode
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

    private fun updateSettings(action: () -> Unit) {
        action.invoke()
        settingsRepo.persist()
    }
}

