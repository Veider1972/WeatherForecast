package ru.veider.weatherforecast.repository

import ru.veider.weatherforecast.data.WeatherQuery


sealed class CitiesLoadingState {
    data class Success(val citiesData: Array<WeatherQuery>) : CitiesLoadingState()
    data class Error(val error: Throwable) : CitiesLoadingState()
    object LoadingState : CitiesLoadingState()
}