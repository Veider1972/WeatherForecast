package ru.veider.weatherforecast.repository.history

import androidx.room.*
import ru.veider.weatherforecast.repository.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity")
    suspend fun getHistory(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistory(entity: HistoryEntity)
}