package ru.veider.weatherforecast.repository

import retrofit2.Callback
import ru.veider.weatherforecast.data.WeatherData


class WeatherRepositoryImpl(private val weatherDataSource: WeatherDataSource) : WeatherRepository {
    override fun getWeatherFromSever(lat: Double, lon: Double, callback: Callback<WeatherData>) {
        weatherDataSource.getWeather(lat, lon, callback)
    }

}