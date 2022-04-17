package ru.veider.weatherforecast.viewmodel

import android.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.repository.weather.DataLoading
import ru.veider.weatherforecast.repository.weather.DataLoading.*
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.repository.cities.CitiesLoadingState
import ru.veider.weatherforecast.repository.cities.CitiesRepository
import ru.veider.weatherforecast.repository.cities.CitiesRepositoryImpl
import ru.veider.weatherforecast.utils.CITIES_KEY

class CitiesViewModel : ViewModel() {

    private val liveCitiesState: MutableLiveData<CitiesLoadingState> = MutableLiveData()
    private val citiesRepositoryImpl: CitiesRepository = CitiesRepositoryImpl()

    fun getCitiesData() = liveCitiesState
    fun getCitiesFromRemoteSource() = getCities()

    var dataLoading: DataLoading = RUSSIAN

    init {
        dataLoading =
            when (PreferenceManager.getDefaultSharedPreferences(WeatherApplication.getInstance())
                .getBoolean(
                    CITIES_KEY, true)) {
                true -> RUSSIAN
                false -> FOREIGN
            }
    }

    private fun getCities() {
        liveCitiesState.value = CitiesLoadingState.LoadingState
        Thread {
            liveCitiesState.postValue(
                CitiesLoadingState.Success(
                    citiesRepositoryImpl.getCitiesFromServer(
                        dataLoading)))
        }.start()
    }
}