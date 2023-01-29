package ru.veider.weatherforecast.repository.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.veider.weatherforecast.repository.entity.WeatherEntity

@Dao
interface WeatherCacheDao {
    @Query("DELETE FROM WeatherEntity WHERE now_time < :datatime")
    fun cleanCache(datatime: Long)

    @Query("SELECT * FROM WeatherEntity WHERE latitude = :latitude AND longitude = :longitude AND now_time > :datatime")
    fun getCache(latitude: Double, longitude: Double, datatime: Long): List<WeatherEntity>

    @Query("SELECT COUNT(*) FROM WeatherEntity WHERE latitude = :latitude AND longitude = :longitude AND now_time > :datatime")
    fun checkCache(latitude: Double, longitude: Double, datatime: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCache(weatherEntity: WeatherEntity)
}