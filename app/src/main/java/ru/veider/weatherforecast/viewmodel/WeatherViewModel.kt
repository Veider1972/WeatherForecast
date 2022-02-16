package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.repository.WeatherRepository
import ru.veider.weatherforecast.repository.WeatherRepositoryImpl

class WeatherViewModel(private val liveWeather:MutableLiveData<WeatherLoading> = MutableLiveData(),
                       private val repositoryImpl: WeatherRepository = WeatherRepositoryImpl()
) : ViewModel() {

    fun getWeatherData() = liveWeather

    fun getWeatherFromLocalSource() = getDataFromLocalSource()
    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveWeather.value = WeatherLoading.Loading
        Thread {
            Thread.sleep(4000)
            liveWeather.postValue(WeatherLoading.Success(repositoryImpl.getWeatherFromLocalStorage()))
        }.start()
    }

}