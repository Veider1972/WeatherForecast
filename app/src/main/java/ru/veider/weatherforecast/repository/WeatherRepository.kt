package ru.veider.weatherforecast.repository

import retrofit2.Callback
import ru.veider.weatherforecast.data.WeatherData

interface WeatherRepository {
    fun getWeatherFromSever(lat: Double, lon: Double, callback: Callback<WeatherData>)
}