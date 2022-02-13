package ru.veider.weatherforecast.repository

import ru.veider.weatherforecast.data.DataLoading
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.data.getForeignCities
import ru.veider.weatherforecast.data.getRussianCities

class CitiesRepositoryImpl : CitiesRepository {
    override fun getCitiesFromServer(dataLoading: DataLoading): Array<WeatherQuery> {
        return if (dataLoading == DataLoading.RUSSIAN) getRussianCities() else getForeignCities()
    }
}