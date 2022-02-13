package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.data.DataLoading
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

    private var dataLoading: DataLoading = DataLoading.RUSSIAN
    private fun getDataLoading(): DataLoading {
       if (dataLoading == DataLoading.RUSSIAN)
           dataLoading = DataLoading.FOREIGN
       else
           dataLoading = DataLoading.RUSSIAN
        return dataLoading
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
            when (getDataLoading()) {
                DataLoading.RUSSIAN -> liveCities.postValue(
                    CitiesLoading.Success(
                        citiesRepositoryImpl.getCitiesFromServer(DataLoading.RUSSIAN)
                    )
                )
                DataLoading.FOREIGN -> liveCities.postValue(
                    CitiesLoading.Success(
                        citiesRepositoryImpl.getCitiesFromServer(DataLoading.FOREIGN)
                    )
                )
            }
        }.start()
    }
}