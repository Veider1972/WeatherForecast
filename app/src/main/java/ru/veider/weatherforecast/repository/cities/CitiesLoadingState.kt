package ru.veider.weatherforecast.repository.cities

import ru.veider.weatherforecast.repository.weather.WeatherQuery


sealed class CitiesLoadingState {
    data class Success(val citiesData: Array<WeatherQuery>) : CitiesLoadingState()
    data class Error(val error: Throwable) : CitiesLoadingState()
    object LoadingState : CitiesLoadingState()
}