package ru.veider.weatherforecast.viewmodel

import ru.veider.weatherforecast.data.WeatherData

sealed class WeatherLoading {
    data class Success(val weatherData: WeatherData) : WeatherLoading()
    data class Error(val error: Throwable) : WeatherLoading()
    object Loading : WeatherLoading()
}

