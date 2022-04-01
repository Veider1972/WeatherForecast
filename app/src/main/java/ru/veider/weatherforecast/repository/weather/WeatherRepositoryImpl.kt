package ru.veider.weatherforecast.repository.weather

import retrofit2.Callback


class WeatherRepositoryImpl(private val weatherWebSource: WeatherWebSource) : WeatherRepository {
    override fun getWeatherFromSever(lat: Double, lon: Double, callback: Callback<WeatherData>) {
        weatherWebSource.getWeather(lat, lon, callback)
    }

}