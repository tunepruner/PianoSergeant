package com.example.musictraining

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class SettingsRepository {
    private val _current: MutableStateFlow<Settings> = MutableStateFlow(Settings())
    val current: StateFlow<Settings> = _current
    init {
        //TODO set up persistence

    }
}