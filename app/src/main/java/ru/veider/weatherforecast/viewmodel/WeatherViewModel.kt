package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Callback
import ru.veider.weatherforecast.cache.WeatherCacheRepository
import ru.veider.weatherforecast.cache.WeatherCacheRepositoryImpl
import ru.veider.weatherforecast.data.WeatherData
import ru.veider.weatherforecast.repository.*
import ru.veider.weatherforecast.utils.showToast
import ru.veider.weatherforecast.view.WeatherApplication
import ru.veider.weatherforecast.R

private const val SERVER_ERROR = "Ошибка сервера"


class WeatherViewModel(
    val weatherLiveData: MutableLiveData<WeatherLoadingState> = MutableLiveData(),
    private val weatherRepositoryImpl: WeatherRepository = WeatherRepositoryImpl(WeatherWebSource()),
    private val cache: WeatherCacheRepository = WeatherCacheRepositoryImpl()
) : ViewModel() {

    private val webCallback = object : Callback<WeatherData> {

        override fun onFailure(call: retrofit2.Call<WeatherData>, t: Throwable) {
            weatherLiveData.postValue(WeatherLoadingState.Error(t))
        }

        override fun onResponse(
            call: retrofit2.Call<WeatherData>, response: retrofit2.Response<WeatherData>
        ) {
            weatherLiveData.postValue(
                if (response.isSuccessful && response.body() != null) {
                    val weatherData: WeatherData = response.body()!!
                    cache.addCache(weatherData)
                    WeatherLoadingState.Success(response.body()!!)
                } else {
                    WeatherLoadingState.Error(Throwable(SERVER_ERROR))
                }
            )
        }
    }

    @DelicateCoroutinesApi
    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        weatherLiveData.value = WeatherLoadingState.Loading
        val state = cache.checkCache(lat, lon)
        if (state) {
            GlobalScope.launch {
                val weatherData = cache.getCache(lat, lon)
                weatherLiveData.postValue(WeatherLoadingState.Success(weatherData))
                cache.cleanCache()
            }
            WeatherApplication.getInstance()
                ?.apply {
                    applicationContext.showToast(getString(R.string.data_from_cache))
                }
        } else {
            weatherRepositoryImpl.getWeatherFromSever(lat, lon, webCallback)
            WeatherApplication.getInstance()
                ?.apply {
                    applicationContext.showToast(getString(R.string.data_from_web))
                }
        }
    }

}