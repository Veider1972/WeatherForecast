package ru.veider.weatherforecast.repository.history

import ru.veider.weatherforecast.repository.weather.*

class HistoryRepositoryImpl(private val historyDataSource: HistoryDataSource) : HistoryRepository {

    override fun getHistory() {
        historyDataSource.getHistory()
    }

    override fun addHistory(weatherData: WeatherData) {
        historyDataSource.addHistory(weatherData)
    }
}