package com.tunepruner.musictraining.viewmodel

import androidx.lifecycle.*
import com.tunepruner.musictraining.model.music.drill.items.TimeConstraint
import com.tunepruner.musictraining.repositories.DrillSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ScaleDrillSettingsViewModel(private val drillSettingsRepo: DrillSettingsRepository) : ViewModel() {
    val settings = drillSettingsRepo.current.asLiveData()

    private var _timeConstraint = MutableLiveData<TimeConstraint>()
    var timeConstraint: LiveData<TimeConstraint> = _timeConstraint

    init {
        viewModelScope.launch {
            drillSettingsRepo.current.collect {
                _timeConstraint.value = it.timeConstraint
            }
        }
    }

    fun enableMetronome() {
        updateSettings {
            settings.value?.timeConstraint = TimeConstraint.METRONOME
            _timeConstraint.value = TimeConstraint.METRONOME
        }
    }

    fun enableRapidFire() {
        updateSettings {
            settings.value?.timeConstraint = TimeConstraint.RAPID_FIRE
            _timeConstraint.value = TimeConstraint.RAPID_FIRE
        }
    }

    private fun updateSettings(action: () -> Unit) {
        action.invoke()
    }
}

