package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.data.DataLoading
import ru.veider.weatherforecast.data.DataLoading.*
import ru.veider.weatherforecast.repository.*

class WeatherViewModel(
    //private val liveWeather: MutableLiveData<WeatherLoading> = MutableLiveData(),
    private val liveCities: MutableLiveData<CitiesLoading> = MutableLiveData(),
    //private val weatherRepositoryImpl: WeatherRepository = WeatherRepositoryImpl(),
    private val citiesRepositoryImpl: CitiesRepository = CitiesRepositoryImpl()
) : ViewModel() {

    //fun getWeatherData() = liveWeather

    fun getCitiesData() = liveCities

//    fun getWeatherFromRemoteSource() = getWeather()
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



//    private fun getWeather() {
//        liveWeather.value = WeatherLoading.Loading
//        Thread {
//            Thread.sleep(500)
//            liveWeather.postValue(WeatherLoading.Success(weatherRepositoryImpl.getRemouteWeather()))
//        }.start()
//    }

    private fun getCities() {
        liveCities.value = CitiesLoading.Loading
        Thread {
            Thread.sleep(500)
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