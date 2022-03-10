package ru.veider.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Callback
import ru.veider.weatherforecast.data.WeatherData
import ru.veider.weatherforecast.repository.WeatherDataSource
import ru.veider.weatherforecast.repository.WeatherLoadingState
import ru.veider.weatherforecast.repository.WeatherRepository
import ru.veider.weatherforecast.repository.WeatherRepositoryImpl

private const val SERVER_ERROR = "Ошибка сервера"

class WeatherViewModel(
    val weatherLiveData: MutableLiveData<WeatherLoadingState> = MutableLiveData(),
    private val weatherRepositoryImpl: WeatherRepository = WeatherRepositoryImpl(WeatherDataSource())
) : ViewModel() {

    private val callback = object : Callback<WeatherData> {

        override fun onFailure(call: retrofit2.Call<WeatherData>, t: Throwable) {
            weatherLiveData.postValue(WeatherLoadingState.Error(t))
        }

        override fun onResponse(
            call: retrofit2.Call<WeatherData>, response: retrofit2.Response<WeatherData>
        ) {
            weatherLiveData.postValue(
                if (response.isSuccessful && response.body() != null) {
                    WeatherLoadingState.Success(response.body()!!)
                } else {
                    WeatherLoadingState.Error(Throwable(SERVER_ERROR))
                }
            )
        }
    }

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        weatherLiveData.value = WeatherLoadingState.Loading
        weatherRepositoryImpl.getWeatherFromSever(lat, lon, callback)
    }
}