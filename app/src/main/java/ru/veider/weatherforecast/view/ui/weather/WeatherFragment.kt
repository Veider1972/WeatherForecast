package ru.veider.weatherforecast.view.ui.weather

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_fragment.*
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.WeatherData
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.databinding.WeatherFragmentBinding
import ru.veider.weatherforecast.utils.showSnack
import ru.veider.weatherforecast.utils.toLatString
import ru.veider.weatherforecast.utils.toLonString
import ru.veider.weatherforecast.repository.WeatherLoadingState
import ru.veider.weatherforecast.viewmodel.WeatherViewModel


class WeatherFragment : Fragment() {

    private var _binder: WeatherFragmentBinding? = null
    private val binder get() = _binder!!
    private val handle = Handler(Looper.getMainLooper())
    private lateinit var weatherQuery: WeatherQuery
    private val weatherViewModel: WeatherViewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            val bundle = arguments
            weatherQuery = bundle?.getParcelable<WeatherQuery>("weather") as WeatherQuery
        }
        _binder = WeatherFragmentBinding.inflate(inflater)

        weatherViewModel.weatherLiveData.observe(
            viewLifecycleOwner,
            Observer { setWeatherMode(it) })
        weatherViewModel.getWeatherFromRemoteSource(weatherQuery.latitude, weatherQuery.longitude)
        return binder.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setWeatherMode(weatherLoadingState: WeatherLoadingState) {
        handle.post {
            with(binder) {
                when (weatherLoadingState) {
                    is WeatherLoadingState.Success -> {
                        loadingLayout.visibility = View.GONE
                        weatherView.visibility = View.VISIBLE
                        setData(weatherLoadingState.weatherData)
                    }
                    is WeatherLoadingState.Error -> {
                        weatherView.visibility = View.GONE
                        loadingLayout.visibility = View.GONE
                        weatherView.showSnack(getString(R.string.error) + ": " + weatherLoadingState.error,
                            getString(R.string.reload),
                            {
                                weatherViewModel.getWeatherFromRemoteSource(
                                    weatherQuery.latitude, weatherQuery.longitude
                                )
                            })
                    }
                    is WeatherLoadingState.Loading -> {
                        weatherView.visibility = View.GONE
                        loadingLayout.visibility = View.VISIBLE
                    }
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
                Picasso.get()
                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    .into(background)
                cityName.text = weatherData.geo_object.district.name
                cityCoordinates.text = String.format(
                    getString(R.string.coordinates),
                    weatherData.info.latitude.toLatString(),
                    weatherData.info.longitude.toLonString()
                )

                conditions.setImageResource(
                    resources.getIdentifier(
                        fact.condition.getIcon(fact.daytime),
                        "drawable",
                        requireActivity().packageName
                    )
                )
                temp.text = fact.temp.toString()
                temperatureFeels.text = fact.feels_like.toString()
                windSpeed.text = fact.wind_speed.toString()
                pressure.text = fact.pressure_mm.toString()
                moisture.text = fact.humidity.toString()

                if (fact.temp_water != null) {
                    waterTemperature.text = fact.temp_water.toString()
                    water_view.visibility = View.VISIBLE
                } else {
                    water_view.visibility = View.GONE
                }

                windDirection.setImageResource(
                    resources.getIdentifier(
                        fact.wind_dir.getDirection(), "drawable", requireActivity().packageName
                    )
                )
            }
        }
    }
}