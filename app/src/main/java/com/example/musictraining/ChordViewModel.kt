package com.example.musictraining

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChordViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _currentChord: MutableLiveData<String?> = MutableLiveData(null)
    val currentChord: LiveData<String?> = _currentChord

}