package com.tunepruner.musictraining.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tunepruner.musictraining.repositories.IncomingMidiSource
import kotlinx.coroutines.launch

class NoteInputViewModel(
    private val midiSource: IncomingMidiSource,
    context: Context
) : ViewModel() {

    val latestNote: LiveData<String?> = midiSource.flowOfNotes.asLiveData()

    init {
        viewModelScope.launch {
//            midiSource.lastNotePersisted.collect {
//                Log.i(LOG_TAG, "saved data = \"$it\" ")
//            }
        }

    }


    fun resendLastNote() {
        viewModelScope.launch {
            midiSource.resendLastNote()
//            dataStore.edit { lastSavedPrefs ->
//                lastSavedPrefs[TESTING] = Math.random().toString()
//                        Log.i(LOG_TAG, "setting...")
//            }
        }

    }

}