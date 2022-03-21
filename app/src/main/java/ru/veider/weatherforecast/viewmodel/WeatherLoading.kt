package ru.veider.weatherforecast.viewmodel

import ru.veider.weatherforecast.data.WeatherData

sealed class WeatherLoading {
    data class Success(val weatherData: WeatherData) : WeatherLoading()
    data class Error(val error: String) : WeatherLoading()
    object Loading : WeatherLoading()
}

