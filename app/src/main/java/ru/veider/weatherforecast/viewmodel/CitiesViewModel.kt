package ru.veider.weatherforecast.viewmodel

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.veider.weatherforecast.data.DataLoading
import ru.veider.weatherforecast.data.DataLoading.*
import ru.veider.weatherforecast.repository.*
import ru.veider.weatherforecast.utils.CITIES_KEY
import ru.veider.weatherforecast.view.WeatherApplication



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

    constructor() : this(MutableLiveData(), CitiesRepositoryImpl()) {
        when(PreferenceManager.getDefaultSharedPreferences(WeatherApplication.getInstance()).getBoolean(CITIES_KEY,true)){
            true -> dataLoading = RUSSIAN
            false -> dataLoading = FOREIGN
        }
    }
}