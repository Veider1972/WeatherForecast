package ru.veider.weatherforecast.repository.history

sealed class HistoryLoadingState {
    data class Success(val historyData: List<HistoryData>) : HistoryLoadingState()
    data class Error(val error: Throwable) : HistoryLoadingState()
    object Loading : HistoryLoadingState()
}

