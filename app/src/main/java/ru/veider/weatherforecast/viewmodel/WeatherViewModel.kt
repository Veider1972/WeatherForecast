package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.data.DataLoading
import ru.veider.weatherforecast.data.DataLoading.*
import ru.veider.weatherforecast.repository.CitiesRepository
import ru.veider.weatherforecast.repository.CitiesRepositoryImpl
import ru.veider.weatherforecast.repository.WeatherRepository
import ru.veider.weatherforecast.repository.WeatherRepositoryImpl

class WeatherViewModel(
    private val liveWeather: MutableLiveData<WeatherLoading> = MutableLiveData(),
    private val liveCities: MutableLiveData<CitiesLoading> = MutableLiveData(),
    private val weatherRepositoryImpl: WeatherRepository = WeatherRepositoryImpl(),
    private val citiesRepositoryImpl: CitiesRepository = CitiesRepositoryImpl()
) : ViewModel() {

    fun getWeatherData() = liveWeather

    fun getCitiesData() = liveCities

    fun getWeatherFromLocalSource() = getDataFromLocalSource()
    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    fun getCitiesFromRemoteSource() = getCities()

    private var dataLoading: DataLoading = RUSSIAN
    private fun DataLoading.getData(): DataLoading {
        return when (this) {
            RUSSIAN -> {
                dataLoading = FOREIGN; FOREIGN
            }
            FOREIGN -> {
                dataLoading = RUSSIAN; RUSSIAN
            }
        }
    }

    private fun getDataFromLocalSource() {
        liveWeather.value = WeatherLoading.Loading
        Thread {
            Thread.sleep(2000)
            liveWeather.postValue(WeatherLoading.Success(weatherRepositoryImpl.getWeatherFromLocalStorage()))
        }.start()
    }

    private fun getCities() {
        liveCities.value = CitiesLoading.Loading
        Thread {
            Thread.sleep(2000)
            liveCities.postValue(
                CitiesLoading.Success(
                    when (dataLoading.getData()) {
                        RUSSIAN -> citiesRepositoryImpl.getCitiesFromServer(RUSSIAN)
                        FOREIGN -> citiesRepositoryImpl.getCitiesFromServer(FOREIGN)
                    }
                )
            )
        }.start()
    }
}