package ru.veider.weatherforecast.repository

import ru.veider.weatherforecast.data.WeatherData

sealed class WeatherLoadingState {
    data class Success(val weatherData: WeatherData) : WeatherLoadingState()
    data class Error(val error: Throwable) : WeatherLoadingState()
    object Loading : WeatherLoadingState()
}

