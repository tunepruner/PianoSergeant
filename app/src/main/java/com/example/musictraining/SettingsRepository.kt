package com.example.musictraining

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SettingsRepository {
    private val _current: MutableStateFlow<Settings> = MutableStateFlow(Settings())
    val current: StateFlow<Settings> = _current

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _current.value.change.collect { _current.value = _current.value }
        }

        //TODO set up persistence

    }
}