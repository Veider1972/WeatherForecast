package ru.gb.kotlinapp.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.veider.weatherforecast.repository.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}