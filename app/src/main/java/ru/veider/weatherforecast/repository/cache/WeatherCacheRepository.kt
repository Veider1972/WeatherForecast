package ru.veider.weatherforecast.repository.cache

import ru.veider.weatherforecast.repository.weather.WeatherData

interface WeatherCacheRepository {
    fun cleanCache()
    fun getCache(latitude: Double, longitude: Double)
    fun checkCache(latitude: Double, longitude: Double)
    fun addCache(weatherData: WeatherData)
}