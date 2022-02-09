package com.tunepruner.musictraining.repositories

//import com.tunepruner.musictraining.data.DrillDatabase
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.tunepruner.musictraining.data.DrillDatabase
import com.tunepruner.musictraining.model.constants.SETTINGS
import com.tunepruner.musictraining.model.music.drill.ChordDrill
import com.tunepruner.musictraining.model.music.drill.Drill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val LOG_TAG = "12345"

@ExperimentalCoroutinesApi
class DrillSettingsRepository(context: Context, val dataStore: DataStore<Preferences>) {
    private var _drillsFlow = MutableStateFlow<List<Drill>>(ArrayList())
    var drillsFlow: StateFlow<List<Drill>> = _drillsFlow

    private val db = Room.databaseBuilder(
        context,
        DrillDatabase::class.java, "drills"
    ).build()
    private val dao = db.dao()

    private var _current = MutableStateFlow(Drill(""))
    val current: StateFlow<Drill> = _current

    fun getAllChordDrills() {
        CoroutineScope(Dispatchers.IO).launch {
            _drillsFlow.value = dao.getAll()
        }
    }

    fun saveDrill(name: String) {
        _current.value.apply {
            if (name.isNotBlank()) {
                this.id = name
                dao.insertAll(this)
                _current.value = this
            }
        }

    }

    fun loadDrill(name: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            name?.let {
                dao.getChordDrill(name).let {
                    _current.value = it
                }
                return@launch
            }

            _current.value = Drill("", ChordDrill())
        }
    }

}