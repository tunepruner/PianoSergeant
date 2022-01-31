package com.tunepruner.musictraining.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunepruner.musictraining.model.music.drill.items.TimeConstraint
import com.tunepruner.musictraining.repositories.DrillSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ChordDrillSettingsViewModel(private val drillSettingsRepo: DrillSettingsRepository) :
    ViewModel() {
    private var _isAddingName = MutableLiveData<Boolean>()
    val isAddingName: LiveData<Boolean> = _isAddingName

    val settings = drillSettingsRepo.current.value

    private var _timeConstraint = MutableLiveData<TimeConstraint>()
    var timeConstraint: LiveData<TimeConstraint> = _timeConstraint

    init {
        _isAddingName.value = false

        viewModelScope.launch {
            drillSettingsRepo.current.collect {
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

    fun promptIfNeedsName() {
        _isAddingName.value = true
    }

    fun stopAddingName() {
        _isAddingName.value = false
    }

    fun saveDrill(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            drillSettingsRepo.saveDrill(name)
        }
    }

    private fun updateSettings(action: () -> Unit) {
        action.invoke()
        drillSettingsRepo.persist()
    }
}

