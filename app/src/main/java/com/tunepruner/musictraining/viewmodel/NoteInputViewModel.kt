package com.tunepruner.musictraining.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tunepruner.musictraining.repositories.IncomingMidiSource

class NoteInputViewModel(
    private val midiSource: IncomingMidiSource,
) : ViewModel() {
    val latestNote: LiveData<String?> = midiSource.flowOfNotes.asLiveData()



}