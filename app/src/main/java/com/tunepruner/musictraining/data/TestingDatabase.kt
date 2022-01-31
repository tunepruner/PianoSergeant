package com.tunepruner.musictraining.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tunepruner.musictraining.model.music.drill.Converters
import com.tunepruner.musictraining.model.music.drill.TestingDrill

@Database(entities = [TestingDrill::class], version = 2)
@TypeConverters(Converters::class)
abstract  class TestingDatabase : RoomDatabase() {
    abstract fun testingDao(): TestingDao
}
