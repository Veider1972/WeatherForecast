package ru.veider.weatherforecast.repository.weather

import retrofit2.Callback

interface WeatherRepository {
    fun getWeatherFromSever(lat: Double, lon: Double, callback: Callback<WeatherData>)
}