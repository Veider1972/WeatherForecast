package ru.veider.weatherforecast.viewmodel

import ru.veider.weatherforecast.data.WeatherQuery


sealed class CitiesLoading {
    data class Success(val citiesData: Array<WeatherQuery>) : CitiesLoading()
    data class Error(val error: Throwable) : CitiesLoading()
    object Loading : CitiesLoading()
}