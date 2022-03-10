package ru.veider.weatherforecast.repository

import retrofit2.Call
import retrofit2.http.*
import ru.veider.weatherforecast.data.WeatherData

interface WeatherAPI {
    @GET("v2/forecast")
    fun getWeatherData(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int = 1,
        @Query("hours") hours: Boolean = false,
        @Query("extra") extra: Boolean = false
    ): Call<WeatherData>

}
