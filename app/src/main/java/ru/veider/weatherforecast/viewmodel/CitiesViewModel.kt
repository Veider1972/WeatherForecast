package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.data.DataLoading
import ru.veider.weatherforecast.data.DataLoading.*
import ru.veider.weatherforecast.repository.*

class CitiesViewModel(
    private val liveCitiesState: MutableLiveData<CitiesLoadingState> = MutableLiveData(),
    private val citiesRepositoryImpl: CitiesRepository = CitiesRepositoryImpl()
) : ViewModel() {

    fun getCitiesData() = liveCitiesState

    fun getCitiesFromRemoteSource() = getCities()

    var dataLoading: DataLoading = RUSSIAN

    private fun getCities() {
        liveCitiesState.value = CitiesLoadingState.LoadingState
        Thread {
            liveCitiesState.postValue(
                CitiesLoadingState.Success(
                    citiesRepositoryImpl.getCitiesFromServer(dataLoading)
                )
            )
        }.start()
    }
}