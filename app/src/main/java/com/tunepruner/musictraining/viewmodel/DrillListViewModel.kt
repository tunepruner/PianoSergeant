package com.tunepruner.musictraining.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunepruner.musictraining.model.music.drill.ChordDrill
import com.tunepruner.musictraining.repositories.DrillSettingsRepository
import com.tunepruner.musictraining.repositories.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrillListViewModel(val repo: DrillSettingsRepository) : ViewModel() {
    private var _allDrills = MutableLiveData<List<ChordDrill>>()
    val allDrills = _allDrills

    init {
        getAllChordDrills()
    }

    fun getAllChordDrills() {
        CoroutineScope(Dispatchers.IO).launch {
            val values = repo.getAllChordDrills()
            viewModelScope.launch {
                _allDrills.value = values
            }
        }
    }

}