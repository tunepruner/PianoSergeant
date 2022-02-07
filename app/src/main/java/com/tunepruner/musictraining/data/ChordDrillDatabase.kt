package com.tunepruner.musictraining.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tunepruner.musictraining.model.music.drill.ChordDrill

@Database(entities = [ChordDrill::class], version = 2)
@TypeConverters(Converters::class)
abstract  class ChordDrillDatabase : RoomDatabase() {
    abstract fun dao(): ChordDrillDao
}
