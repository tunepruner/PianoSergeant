package com.tunepruner.musictraining.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tunepruner.musictraining.model.music.drill.TestingDrill

@Dao
interface TestingDao {
    @Query("SELECT * FROM testingdrill")
    fun getAll(): List<TestingDrill>

//    @Query("SELECT * FROM testingdrill WHERE drillId IN (:ids)")
//    fun loadAllByIds(ids: IntArray): List<TestingDrill>

//    @Query("SELECT * FROM testingdrill WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): TestingDrill

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg drills: TestingDrill)
//
//
//    @Delete
//    fun delete(drill: TestingDrill)

}