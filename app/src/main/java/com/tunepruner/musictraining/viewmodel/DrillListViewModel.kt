package com.tunepruner.musictraining.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tunepruner.musictraining.model.music.drill.ChordDrill
import com.tunepruner.musictraining.repositories.DrillSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DrillListViewModel(val repo: DrillSettingsRepository) : ViewModel() {
    private var _allDrills = repo.drillsFlow.asLiveData()
    val allDrills: LiveData<List<ChordDrill>> = _allDrills

    fun getAllChordDrills() {
        repo.getAllChordDrills()
    }
}