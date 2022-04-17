package ru.veider.weatherforecast.repository.cache

import kotlinx.coroutines.DelicateCoroutinesApi
import ru.veider.weatherforecast.repository.weather.*

@DelicateCoroutinesApi
class WeatherCacheRepositoryImpl(private val weatherCacheDataSource: WeatherCacheDataSource) : WeatherCacheRepository {

    override fun cleanCache() {
        weatherCacheDataSource.cleanCache()
    }

    override fun getCache(latitude: Double, longitude: Double) {
        weatherCacheDataSource.getCache(latitude, longitude)
    }

    override fun checkCache(latitude: Double, longitude: Double) {
        weatherCacheDataSource.checkCache(latitude, longitude)
    }

    override fun addCache(weatherData: WeatherData) {
        weatherCacheDataSource.addCache(weatherData)
    }
}