package com.tunepruner.musictraining.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunepruner.musictraining.model.music.drill.items.TimeConstraint
import com.tunepruner.musictraining.repositories.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ScaleDrillSettingsViewModel(private val settingsRepo: SettingsRepository) : ViewModel() {
    val settings = settingsRepo.current.value

    private var _timeConstraint = MutableLiveData<TimeConstraint>()
    var timeConstraint: LiveData<TimeConstraint> = _timeConstraint

    init {
        viewModelScope.launch {
            settingsRepo.current.collect {
                _timeConstraint.value = it.timeConstraint
            }
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

