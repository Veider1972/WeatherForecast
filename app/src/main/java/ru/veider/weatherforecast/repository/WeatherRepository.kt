package ru.veider.weatherforecast.repository

import ru.veider.weatherforecast.data.WeatherData

interface WeatherRepository {
    fun getWeatherFromServer(): WeatherData
    fun getWeatherFromLocalStorage(): WeatherData
}