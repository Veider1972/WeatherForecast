package ru.veider.weatherforecast.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.veider.weatherforecast.repository.history.HistoryDao
import ru.veider.weatherforecast.repository.cache.WeatherCacheDao
import ru.veider.weatherforecast.repository.entity.HistoryEntity
import ru.veider.weatherforecast.repository.entity.WeatherEntity

@Database(entities = [WeatherEntity::class, HistoryEntity::class], version = 1, exportSchema = false)
abstract class WeatherDataBase:RoomDatabase() {
    abstract fun weatherCacheDao(): WeatherCacheDao
    abstract fun historyDao(): HistoryDao
}