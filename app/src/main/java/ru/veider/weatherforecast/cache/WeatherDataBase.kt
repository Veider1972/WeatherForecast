package ru.veider.weatherforecast.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDataBase:RoomDatabase() {
    abstract fun weatherCacheDao():WeatherCacheDao
}