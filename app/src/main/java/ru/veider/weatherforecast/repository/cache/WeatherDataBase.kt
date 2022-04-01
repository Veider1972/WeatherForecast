package ru.veider.weatherforecast.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.veider.weatherforecast.repository.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDataBase:RoomDatabase() {
    abstract fun weatherCacheDao():WeatherCacheDao
}