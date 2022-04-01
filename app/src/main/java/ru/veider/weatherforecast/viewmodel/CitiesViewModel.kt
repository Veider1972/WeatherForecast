package ru.veider.weatherforecast.viewmodel

import android.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.repository.weather.DataLoading
import ru.veider.weatherforecast.repository.weather.DataLoading.*
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.repository.CITIES_KEY
import ru.veider.weatherforecast.repository.cities.CitiesLoadingState
import ru.veider.weatherforecast.repository.cities.CitiesRepository
import ru.veider.weatherforecast.repository.cities.CitiesRepositoryImpl


class CitiesViewModel(
        private val liveCitiesState: MutableLiveData<CitiesLoadingState> = MutableLiveData(),
        private val citiesRepositoryImpl: CitiesRepository = CitiesRepositoryImpl(),
                     ) : ViewModel() {

    fun getCitiesData() = liveCitiesState

    fun getCitiesFromRemoteSource() = getCities()

    var dataLoading: DataLoading = RUSSIAN

    private fun getCities() {
        liveCitiesState.value = CitiesLoadingState.LoadingState
        Thread {
            liveCitiesState.postValue(CitiesLoadingState.Success(citiesRepositoryImpl.getCitiesFromServer(dataLoading)))
        }.start()
    }

    init {
        dataLoading = when (PreferenceManager.getDefaultSharedPreferences(WeatherApplication.getInstance())
                .getBoolean(CITIES_KEY,
                            true)) {
            true -> RUSSIAN
            false -> FOREIGN
        }
    }
}