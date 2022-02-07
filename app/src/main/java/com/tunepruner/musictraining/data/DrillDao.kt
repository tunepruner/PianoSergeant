package com.tunepruner.musictraining.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tunepruner.musictraining.model.music.drill.Drill

@Dao
interface DrillDao {
    @Query("SELECT * FROM drills")
    fun getAll(): List<Drill>

    @Query("SELECT * FROM drills WHERE id LIKE :name LIMIT 1")
    fun getChordDrill(name: String): Drill

    @Query("SELECT * FROM drills WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Drill>

//    @Query("SELECT * FROM users WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): ChordDrill

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg drills: Drill)

    @Delete
    fun delete(drill: Drill)
}