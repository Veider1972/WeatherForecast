package ru.gb.kotlinapp.model.room

import androidx.room.*
import ru.veider.weatherforecast.repository.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity")
    fun getHistory(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHistory(entity: HistoryEntity)
}