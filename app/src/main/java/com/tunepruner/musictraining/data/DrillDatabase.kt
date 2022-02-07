package com.tunepruner.musictraining.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tunepruner.musictraining.model.music.drill.Drill

@Database(entities = [Drill::class], version = 2)
@TypeConverters(Converters::class)
abstract  class DrillDatabase : RoomDatabase() {
    abstract fun dao(): DrillDao
}
