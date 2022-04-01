package ru.veider.weatherforecast.repository.cache

import ru.veider.weatherforecast.repository.weather.WeatherData

interface WeatherCacheRepository {
    fun cleanCache()
    fun getCache(
            latitude: Double,
            longitude: Double,
                ): WeatherData

    fun checkCache(
            latitude: Double,
            longitude: Double,
                  ): Boolean

    fun addCache(weatherData: WeatherData)
}