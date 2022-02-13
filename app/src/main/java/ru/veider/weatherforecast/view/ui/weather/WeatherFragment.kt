package ru.veider.weatherforecast.view.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.WeatherData
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.databinding.WeatherFragmentBinding
import ru.veider.weatherforecast.utils.Utils
import ru.veider.weatherforecast.viewmodel.WeatherLoading
import ru.veider.weatherforecast.viewmodel.WeatherViewModel

class WeatherFragment : Fragment(), Utils {

    private var _binder: WeatherFragmentBinding? = null
    private val binder get() = _binder!!
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherQuery: WeatherQuery

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            val bundle = arguments
            weatherQuery = bundle?.getParcelable<WeatherQuery>("weather") as WeatherQuery
        }
        _binder = WeatherFragmentBinding.inflate(inflater)
        val citiesView = binder.root
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        val weatherData = Observer<WeatherLoading> { getWeatherData(it) }
        weatherViewModel.getWeatherData().observe(this.viewLifecycleOwner, weatherData)
        weatherViewModel.getWeatherFromLocalSource()
        return citiesView
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

    override fun onPause() {
        super.onPause()
        _binder = null
    }

    private fun setData(weatherData: WeatherData) {
        binder.cityName.text = weatherQuery.name
        binder.temp.text = weatherData.fact.temp.toString()
        binder.sunrise.text = weatherData.forecast.sunrise
        binder.sunset.text = weatherData.forecast.sunset
        binder.temperatureFeels.text = weatherData.fact.feels_like.toString()
        binder.windDirection.setImageResource(
            resources.getIdentifier(
                weatherData.fact.wind_dir.getDirection(), "drawable", requireActivity().packageName
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