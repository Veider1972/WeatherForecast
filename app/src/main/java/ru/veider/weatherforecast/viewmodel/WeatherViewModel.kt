package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Callback
import ru.veider.weatherforecast.repository.cache.WeatherCacheRepositoryImpl
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.repository.weather.*
import ru.veider.weatherforecast.ui.utils.showToast

private const val SERVER_ERROR = "Ошибка сервера"

class WeatherViewModel(
        val weatherLiveData: MutableLiveData<WeatherLoadingState> = MutableLiveData(),
        private val weatherRepositoryImpl: WeatherRepositoryImpl = WeatherRepositoryImpl(WeatherWebSource()),
        private val cache: WeatherCacheRepositoryImpl = WeatherCacheRepositoryImpl(),
                      ) : ViewModel() {

    @DelicateCoroutinesApi
    private val webCallback = object : Callback<WeatherData> {

        override fun onFailure(
                call: retrofit2.Call<WeatherData>,
                t: Throwable,
                              ) {
            weatherLiveData.postValue(WeatherLoadingState.Error(t))
        }

        override fun onResponse(
                call: retrofit2.Call<WeatherData>,
                response: retrofit2.Response<WeatherData>,
                               ) {
            weatherLiveData.postValue(if (response.isSuccessful && response.body() != null) {
                val weatherData: WeatherData = response.body()!!
                GlobalScope.launch {
                    cache.addCache(weatherData)
                }
                WeatherLoadingState.Success(response.body()!!)
            } else {
                WeatherLoadingState.Error(Throwable(SERVER_ERROR))
            })
        }
    }

    @DelicateCoroutinesApi
    fun getWeatherFromRemoteSource(
            lat: Double,
            lon: Double,
                                  ) {
        weatherLiveData.value = WeatherLoadingState.Loading
        val state = cache.checkCache(lat,
                                     lon)
        if (state) {
            GlobalScope.launch {
                cache.cleanCache()
            }
            GlobalScope.launch {
                val weatherData = cache.getCache(lat,
                                                 lon)
                weatherLiveData.postValue(WeatherLoadingState.Success(weatherData))
            }
            WeatherApplication.getInstance()
                    ?.apply {
                        applicationContext.showToast(getString(R.string.data_from_cache))
                    }
        } else {
            weatherRepositoryImpl.getWeatherFromSever(lat,
                                                      lon,
                                                      webCallback)
            WeatherApplication.getInstance()
                    ?.apply {
                        applicationContext.showToast(getString(R.string.data_from_web))
                    }
        }
    }

}