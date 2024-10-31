package ru.lostgirl.mymapapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.lostgirl.mymapapp.db.entity.MarkerEntity

@Dao
interface MarkerDao {
    @Query("SELECT * FROM MarkerEntity")
    fun getAll(): List<MarkerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(post: MarkerEntity): Long

    @Query("DELETE FROM MarkerEntity WHERE id = :id")
    suspend fun removeById(id: Int)
}