package com.tunepruner.musictraining.model.constants

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val LAST_NOTE = stringPreferencesKey("last_note")
val SETTINGS = stringPreferencesKey("settings")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "music_training")