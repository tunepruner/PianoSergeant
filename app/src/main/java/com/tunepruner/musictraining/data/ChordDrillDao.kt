package com.tunepruner.musictraining.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tunepruner.musictraining.model.music.drill.ChordDrill

@Dao
interface ChordDrillDao {
    @Query("SELECT * FROM chordDrill")
    fun getAll(): List<ChordDrill>

    @Query("SELECT * FROM chordDrill WHERE id LIKE :name LIMIT 1")
    fun getChordDrill(name: String): ChordDrill

    @Query("SELECT * FROM chordDrill WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<ChordDrill>

//    @Query("SELECT * FROM users WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): ChordDrill

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg drills: ChordDrill)

    @Delete
    fun delete(drill: ChordDrill)
}