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
import ru.veider.weatherforecast.utils.showSnack
import ru.veider.weatherforecast.utils.toLatString
import ru.veider.weatherforecast.utils.toLonString
import ru.veider.weatherforecast.viewmodel.WeatherLoading
import ru.veider.weatherforecast.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {

    private var _binder: WeatherFragmentBinding? = null
    private val binder get() = _binder!!
    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(this).get(
            WeatherViewModel::class.java
        )
    }
    private lateinit var weatherQuery: WeatherQuery

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            val bundle = arguments
            weatherQuery = bundle?.getParcelable<WeatherQuery>("weather") as WeatherQuery
        }
        _binder = WeatherFragmentBinding.inflate(inflater)
        val weatherData = Observer<WeatherLoading> { getWeatherData(it) }
        with(weatherViewModel) {
            getWeatherData().observe(this@WeatherFragment.viewLifecycleOwner, weatherData)
            getWeatherFromLocalSource()
        }

        return binder.root
    }

    private fun getWeatherData(weatherLoading: WeatherLoading) {
        with(binder) {
            when (weatherLoading) {
                is WeatherLoading.Success -> {
                    loadingLayout.visibility = View.GONE
                    weatherView.visibility = View.VISIBLE
                    setData(weatherLoading.weatherData)
                }
                is WeatherLoading.Error -> {
                    weatherView.visibility = View.GONE
                    loadingLayout.visibility = View.GONE
                    weatherView.showSnack(getString(R.string.error),
                        getString(R.string.reload),
                        { weatherViewModel.getWeatherFromLocalSource() })
                }
                is WeatherLoading.Loading -> {
                    weatherView.visibility = View.GONE
                    loadingLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        _binder = null
    }

    private fun setData(weatherData: WeatherData) {
        with(binder) {
            with(weatherData) {
                cityName.text = weatherQuery.name
                cityCoordinates.text = String.format(
                    getString(R.string.coordinates),
                    weatherQuery.latitude.toLatString(),
                    weatherQuery.longitude.toLonString()
                )
                temp.text = fact.temp.toString()
                sunrise.text = forecast.sunrise
                sunset.text = forecast.sunset
                temperatureFeels.text = fact.feels_like.toString()
                pressure.text = fact.pressure_mm.toString()
                moisture.text = fact.humidity.toString()
                waterTemperature.text = if (fact.temp_water != null) fact.temp_water.toString() else "-"
                windDirection.setImageResource(
                    resources.getIdentifier(
                        fact.wind_dir.getDirection(), "drawable", requireActivity().packageName
                    )
                )
            }
            forecastListView.adapter = WeatherAdapter(
                this@WeatherFragment, weatherData.forecast.parts
            )
        }
    }
}