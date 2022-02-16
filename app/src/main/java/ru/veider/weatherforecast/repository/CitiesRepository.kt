package ru.veider.weatherforecast.repository

import ru.veider.weatherforecast.data.DataLoading
import ru.veider.weatherforecast.data.WeatherQuery

interface CitiesRepository {
    fun getCitiesFromServer(dataLoading: DataLoading): Array<WeatherQuery>
}