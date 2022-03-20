package ru.veider.weatherforecast.repository.cities

import ru.veider.weatherforecast.repository.weather.DataLoading
import ru.veider.weatherforecast.repository.weather.WeatherQuery

interface CitiesRepository {
    fun getCitiesFromServer(dataLoading: DataLoading): Array<WeatherQuery>
}