package ru.veider.weatherforecast.repository.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val now_time: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val district_id: Int? = 0,
    val district_name: String? = "",
    val locality_id: Int? = 0,
    val locality_name: String? = "",
    val province_id: Int = 0,
    val province_name: String = "",
    val country_id: Int = 0,
    val country_name: String = "",
    val temp: Int = 0,
    val feels_like: Int = 0,
    val temp_water: Int? = 0,
    val condition: Int = 0,
    val wind_speed: Double = 0.0,
    val wind_dir: Int = 0,
    val pressure_mm: Int = 0,
    val humidity: Int = 0,
    val daytime: Int = 0)