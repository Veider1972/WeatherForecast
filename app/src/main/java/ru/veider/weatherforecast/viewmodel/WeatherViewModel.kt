package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.data.DataLoading
import ru.veider.weatherforecast.data.DataLoading.*
import ru.veider.weatherforecast.repository.*

class WeatherViewModel(
    private val liveCities: MutableLiveData<CitiesLoading> = MutableLiveData(),
    private val citiesRepositoryImpl: CitiesRepository = CitiesRepositoryImpl()
) : ViewModel() {

    fun getCitiesData() = liveCities

    fun getCitiesFromRemoteSource() = getCities()

    var dataLoading: DataLoading = RUSSIAN

    private fun getCities() {
        liveCities.value = CitiesLoading.Loading
        Thread {
            Thread.sleep(500)
            liveCities.postValue(
                CitiesLoading.Success(
                    citiesRepositoryImpl.getCitiesFromServer(dataLoading)
                )
            )
        }.start()
    }
}