package ru.veider.weatherforecast.cache

import ru.veider.weatherforecast.data.WeatherData

interface WeatherCacheRepository {
    fun cleanCache()
    fun getCache(latitude: Double, longitude:Double): WeatherData
    fun checkCache(latitude: Double, longitude:Double):Boolean
    fun addCache(weatherData:WeatherData)
}