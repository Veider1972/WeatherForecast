package ru.veider.weatherforecast.repository.weather

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.veider.weatherforecast.BuildConfig
import ru.veider.weatherforecast.utils.TAG
import java.io.IOException

class WeatherWebSource {

    private val weatherAPI =
        Retrofit.Builder().baseUrl("https://api.weather.yandex.ru/").addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(WeatherInterceptor())).build().create(WeatherAPI::class.java)

    fun getWeather(
        lat: Double, lon: Double, callback: Callback<WeatherData>) {
        weatherAPI.getWeatherData(
            BuildConfig.YANDEX_API_KEY, lat, lon).enqueue(callback)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return okHttpClient.build()
    }

    inner class WeatherInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            Log.d(TAG, chain.request().toString())
            return chain.proceed(chain.request())
        }
    }
}