package ru.veider.weatherforecast.repository.cities

import ru.veider.weatherforecast.repository.weather.DataLoading
import ru.veider.weatherforecast.repository.weather.WeatherQuery
import ru.veider.weatherforecast.repository.weather.getForeignCities
import ru.veider.weatherforecast.repository.weather.getRussianCities

class CitiesRepositoryImpl : CitiesRepository {
    override fun getCitiesFromServer(dataLoading: DataLoading): Array<WeatherQuery> {
        return if (dataLoading == DataLoading.RUSSIAN) getRussianCities() else getForeignCities()
    }
}