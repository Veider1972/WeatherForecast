package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.repository.history.HistoryLoadingState
import ru.veider.weatherforecast.repository.history.HistoryRepositoryImpl

class HistoryViewModel(
        val historyLiveData: MutableLiveData<HistoryLoadingState> = MutableLiveData(),
        private val historyRepositoryImpl: HistoryRepositoryImpl = HistoryRepositoryImpl(),
                      ) : ViewModel() {

    fun getAllHistory() {
        historyLiveData.value = HistoryLoadingState.Loading
        historyLiveData.value = HistoryLoadingState.Success(historyRepositoryImpl.getHistory())
    }
}