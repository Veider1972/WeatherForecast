package ru.veider.weatherforecast.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.WeatherData
import ru.veider.weatherforecast.databinding.MainActivityBinding
import ru.veider.weatherforecast.viewmodel.WeatherLoading
import ru.veider.weatherforecast.viewmodel.WeatherViewModel

class WeatherView : AppCompatActivity() {

    private lateinit var binder: MainActivityBinding
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = MainActivityBinding.inflate(layoutInflater)

        setContentView(binder.root)

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        val weatherData = Observer<WeatherLoading> { getWeatherData(it) }
        weatherViewModel.getWeatherData().observe(this, weatherData)
        weatherViewModel.getWeatherFromLocalSource()
    }

    private fun getWeatherData(weatherLoading: WeatherLoading) {
        when (weatherLoading) {
            is WeatherLoading.Success -> {
                val weatherData = weatherLoading.weatherData
                binder.loadingLayout.visibility = View.GONE
                binder.weatherView.visibility = View.VISIBLE
                setData(weatherData)
            }
            is WeatherLoading.Error -> {
                binder.weatherView.visibility = View.GONE
                binder.loadingLayout.visibility = View.GONE

                Snackbar.make(
                    binder.weatherView, getString(R.string.error), Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.reload)) {
                    weatherViewModel.getWeatherFromLocalSource()
                }.show()
            }
            is WeatherLoading.Loading -> {
                binder.weatherView.visibility = View.GONE
                binder.loadingLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setData(weatherData: WeatherData) {
        binder.cityName.text = "Москва"   // Потом подумаю, откуда название брать
        binder.temp.text = weatherData.fact.temp.toString()
        binder.sunrise.text = weatherData.forecast.sunrise
        binder.sunset.text = weatherData.forecast.sunset
        binder.temperatureFeels.text = weatherData.fact.feels_like.toString()
        binder.windDirection.setImageResource(
            resources.getIdentifier(
                weatherData.fact.wind_dir.getDirection(), "drawable", this.packageName
            )
        )
        binder.pressure.text = weatherData.fact.pressure_mm.toString()
        binder.humidity.text = weatherData.fact.humidity.toString()
        binder.waterTemperature.text =
            if (weatherData.fact.temp_water != null) weatherData.fact.temp_water.toString() else "-"
        val weatherListView = binder.forecastListView
        val weatherAdapter = WeatherAdapter(this, weatherData.forecast.parts)
        weatherListView.adapter = weatherAdapter
    }
}