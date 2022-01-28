package com.tunepruner.musictraining.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tunepruner.musictraining.repositories.Mode

class SettingsViewModel : ViewModel() {
    private var _mode = MutableLiveData<Mode>()
    var mode: LiveData<Mode> = _mode

    fun enableChordMode() { _mode.value = Mode.CHORD }

    fun enableScaleMode() { _mode.value = Mode.SCALE }
}

