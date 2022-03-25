package ru.veider.weatherforecast.repository

import retrofit2.Callback
import ru.veider.weatherforecast.data.WeatherData


class WeatherRepositoryImpl(private val weatherWebSource: WeatherWebSource) : WeatherRepository {
    override fun getWeatherFromSever(lat: Double, lon: Double, callback: Callback<WeatherData>) {
        weatherWebSource.getWeather(lat, lon, callback)
    }

}