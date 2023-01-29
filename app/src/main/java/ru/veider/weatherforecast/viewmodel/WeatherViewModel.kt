package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import retrofit2.Callback
import ru.veider.weatherforecast.repository.cache.WeatherCacheRepositoryImpl
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.repository.cache.WeatherCacheDataSource
import ru.veider.weatherforecast.repository.weather.*
import ru.veider.weatherforecast.ui.utils.showToast

private const val SERVER_ERROR = "Ошибка сервера"

@DelicateCoroutinesApi
class WeatherViewModel : ViewModel(), WeatherCacheDataSource.WeatherCacheEvent {
    val weatherLiveData: MutableLiveData<WeatherLoadingState> = MutableLiveData()
    private val weatherRepositoryImpl: WeatherRepositoryImpl =
        WeatherRepositoryImpl(WeatherWebSource())
    private val cache: WeatherCacheRepositoryImpl =
        WeatherCacheRepositoryImpl(WeatherCacheDataSource.getInstance(this))

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val webCallback = object : Callback<WeatherData> {

        override fun onFailure(
            call: retrofit2.Call<WeatherData>, t: Throwable) {
            weatherLiveData.postValue(WeatherLoadingState.Error(t))
        }

        override fun onResponse(
            call: retrofit2.Call<WeatherData>, response: retrofit2.Response<WeatherData>) {
            uiScope.launch {
                weatherLiveData.postValue(if (response.isSuccessful && response.body() != null) {
                    val weatherData: WeatherData = response.body()!!
                    cache.addCache(weatherData)
                    withContext(Dispatchers.Main) {
                        WeatherApplication.getInstance()
                            ?.apply { applicationContext.showToast(getString(R.string.data_from_web)) }
                    }
                    WeatherLoadingState.Success(response.body()!!)
                } else {
                    WeatherLoadingState.Error(Throwable(SERVER_ERROR))
                })
            }
        }
    }

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        weatherLiveData.value = WeatherLoadingState.Loading
        cache.checkCache(lat, lon)

    }

    override fun getWeatherCache(weatherData: WeatherData) {
        uiScope.launch {
            weatherLiveData.postValue(WeatherLoadingState.Success(weatherData))
            withContext(Dispatchers.Main) {
                WeatherApplication.getInstance()?.apply {
                    applicationContext.showToast(getString(R.string.data_from_cache))
                }
            }
        }
    }


    override fun checkCacheData(latitude: Double, longitude: Double, hasData: Boolean) {
        if (hasData) {
            cache.cleanCache()
            cache.getCache(latitude, longitude)
        } else {
            weatherRepositoryImpl.getWeatherFromSever(
                latitude, longitude, webCallback)

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}