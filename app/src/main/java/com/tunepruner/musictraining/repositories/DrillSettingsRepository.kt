package com.tunepruner.musictraining.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.tunepruner.musictraining.data.TestingDatabase
//import com.tunepruner.musictraining.data.TestingDatabase
import com.tunepruner.musictraining.model.constants.SETTINGS
import com.tunepruner.musictraining.model.music.drill.ChordDrill
import com.tunepruner.musictraining.model.music.drill.TestingDrill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val LOG_TAG = "12345"

@ExperimentalCoroutinesApi
class DrillSettingsRepository(context: Context, val dataStore: DataStore<Preferences>) {

    val db = Room.databaseBuilder(
        context,
        TestingDatabase::class.java, "testingdrill"
    ).build()

    val testingDao = db.testingDao()


    var savedSettingsFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[SETTINGS] ?: ""
    }
    private val _current: MutableStateFlow<ChordDrill> = MutableStateFlow(ChordDrill(1))/*TODO this number needs to be generated dynamically somehow*/
    val current: StateFlow<ChordDrill> = _current

    fun persist() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit {
                it[SETTINGS] = GsonBuilder().create().toJson(current.value)
            }
        }
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            testingDao.insertAll(TestingDrill("first try"))
            val users: List<TestingDrill> = testingDao.getAll()
            Log.i(LOG_TAG, "users = $users")

            savedSettingsFlow.collect {
                _current.value =
                    GsonBuilder().create().fromJson(it, ChordDrill::class.java) ?: ChordDrill(1)/*TODO same here*/
            }
        }
    }
}