package com.tunepruner.musictraining.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tunepruner.musictraining.model.music.drill.ChordDrill
import com.tunepruner.musictraining.model.music.drill.Drill
import com.tunepruner.musictraining.model.music.drill.ScaleDrill
import com.tunepruner.musictraining.model.music.drill.items.ChordQuality
import com.tunepruner.musictraining.model.music.drill.items.Inversion
import com.tunepruner.musictraining.model.music.drill.items.Key
import java.lang.reflect.Type
import java.util.*

class Converters {

    @TypeConverter
    fun storedStringToChordQualities(chordQualities: String?): Set<ChordQuality?>? {
        val gson = Gson()
        if (chordQualities == null) {
            return Collections.emptySet()
        }
        val listType: Type = object : TypeToken<Set<ChordQuality?>?>() {}.type
        return gson.fromJson<Set<ChordQuality?>>(chordQualities, listType)
    }

    @TypeConverter
    fun chordQualitiesToStoredString(chordQualities: Set<ChordQuality?>?): String? {
        val gson = Gson()
        return gson.toJson(chordQualities)
    }

    @TypeConverter
    fun storedStringToInversion(data: String?): Set<Inversion?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptySet()
        }
        val listType: Type = object : TypeToken<Set<Inversion?>?>() {}.type
        return gson.fromJson<Set<Inversion?>>(data, listType)
    }

    @TypeConverter
    fun inversionToStoredString(myObjects: Set<Inversion?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

    @TypeConverter
    fun storedStringToKeys(data: String?): Set<Key?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptySet()
        }
        val listType: Type = object : TypeToken<Set<Key?>?>() {}.type
        return gson.fromJson<Set<Key?>>(data, listType)
    }

    @TypeConverter
    fun keyToStoredString(myObjects: Set<Key?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

    @TypeConverter
    fun storedStringToChordDrill(data: String?): ChordDrill? {
        val gson = Gson()
        if (data == null) {
            return null
        }
        val listType: Type = object : TypeToken<ChordDrill?>() {}.type
        return gson.fromJson<ChordDrill?>(data, listType)
    }

    @TypeConverter
    fun chordDrillToStoredString(myObject: ChordDrill?): String? {
        val gson = Gson()
        return gson.toJson(myObject)
    }

    @TypeConverter
    fun storedStringToScaleDrill(data: String?): ScaleDrill? {
        val gson = Gson()
        if (data == null) {
            return null
        }
        val listType: Type = object : TypeToken<ScaleDrill?>() {}.type
        return gson.fromJson<ScaleDrill?>(data, listType)
    }

    @TypeConverter
    fun scaleDrillToStoredString(myObject: ScaleDrill?): String? {
        val gson = Gson()
        return gson.toJson(myObject)
    }

    @TypeConverter
    fun storedStringToDrill(data: String?): Drill? {
        val gson = Gson()
        if (data == null) {
            return null
        }
        val listType: Type = object : TypeToken<Drill?>() {}.type
        return gson.fromJson<Drill?>(data, listType)
    }

    @TypeConverter
    fun drillToStoredString(myObject: Drill?): String? {
        val gson = Gson()
        return gson.toJson(myObject)
    }
}