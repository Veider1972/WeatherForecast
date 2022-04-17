package ru.veider.weatherforecast.repository.history

import ru.veider.weatherforecast.repository.weather.WeatherData

interface HistoryRepository {
    fun getHistory()
    fun addHistory(weatherData: WeatherData)
}