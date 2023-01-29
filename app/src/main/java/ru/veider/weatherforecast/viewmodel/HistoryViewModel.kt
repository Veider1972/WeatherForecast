package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.repository.history.HistoryData
import ru.veider.weatherforecast.repository.history.HistoryDataSource
import ru.veider.weatherforecast.repository.history.HistoryLoadingState
import ru.veider.weatherforecast.repository.history.HistoryRepositoryImpl
import ru.veider.weatherforecast.repository.weather.WeatherData

class HistoryViewModel : ViewModel(), HistoryDataSource.HistoryEvents {

    val historyLiveData: MutableLiveData<HistoryLoadingState> = MutableLiveData()
    private val historyRepositoryImpl: HistoryRepositoryImpl =
        HistoryRepositoryImpl(HistoryDataSource.getInstance(this))

    fun getAllHistory() {
        historyLiveData.value = HistoryLoadingState.Loading
        historyRepositoryImpl.getHistory()
    }

    override fun getHistoryData(historyData: List<HistoryData>) {
        historyLiveData.postValue(HistoryLoadingState.Success(historyData))
    }

    fun addHistory(weatherData: WeatherData) {
        HistoryRepositoryImpl(HistoryDataSource.getInstance(this)).addHistory(weatherData)
    }
}