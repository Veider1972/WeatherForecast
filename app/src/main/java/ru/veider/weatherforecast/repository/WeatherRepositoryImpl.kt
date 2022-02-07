package ru.veider.weatherforecast.repository

import ru.veider.weatherforecast.data.WeatherData
import ru.veider.weatherforecast.data.testWeatherData

class WeatherRepositoryImpl:WeatherRepository {
    override fun getWeatherFromServer(): WeatherData {
        return testWeatherData()
    }

    override fun getWeatherFromLocalStorage(): WeatherData {
        return testWeatherData()
    }
}