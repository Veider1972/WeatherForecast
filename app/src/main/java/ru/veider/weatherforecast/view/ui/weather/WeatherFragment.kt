package ru.veider.weatherforecast.view.ui.weather

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.weather_fragment.*
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.DayTime
import ru.veider.weatherforecast.data.WeatherData
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.databinding.WeatherFragmentBinding
import ru.veider.weatherforecast.repository.*
import ru.veider.weatherforecast.utils.showSnack
import ru.veider.weatherforecast.utils.toLatString
import ru.veider.weatherforecast.utils.toLonString
import ru.veider.weatherforecast.viewmodel.WeatherLoading
import java.lang.Exception

class WeatherFragment : Fragment() {

    private var _binder: WeatherFragmentBinding? = null
    private val binder get() = _binder!!
    private val handle = Handler(Looper.getMainLooper())
    private lateinit var weatherQuery: WeatherQuery
    private val weatherBroadcastReceiver = WeatherBroadcastReceiver()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            val bundle = arguments
            weatherQuery = bundle?.getParcelable<WeatherQuery>("weather") as WeatherQuery
        }
        _binder = WeatherFragmentBinding.inflate(inflater)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(
                weatherBroadcastReceiver, IntentFilter(INTENT_WEATHER)
            )
        WeatherIntentService.startWeatherService(
            requireContext(), weatherQuery.latitude, weatherQuery.longitude
        )

        return binder.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setWeatherMode(weatherLoading: WeatherLoading) {
        handle.post {
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
                        weatherView.showSnack(getString(R.string.error) + ": " + weatherLoading.error,
                            getString(R.string.reload),
                            {
                                WeatherIntentService.startWeatherService(
                                    requireContext(), weatherQuery.latitude, weatherQuery.longitude
                                )
                            })
                    }
                    is WeatherLoading.Loading -> {
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
                cityName.text = weatherData.geo_object?.district?.name
                    ?: weatherData.geo_object?.locality?.name ?: getString(R.string.error)
                cityCoordinates.text = String.format(
                    getString(R.string.coordinates),
                    weatherData.info?.latitude?.toLatString() ?: getString(R.string.error),
                    weatherData.info?.longitude?.toLonString() ?: getString(R.string.error)
                )
                conditions.setImageResource(
                    resources.getIdentifier(
                        fact?.condition?.getIcon(fact.daytime ?: DayTime.DAY),
                        "drawable",
                        requireActivity().packageName
                    )
                )
                temp.text = fact?.temp?.toString()
                temperatureFeels.text = fact?.feels_like.toString()
                windSpeed.text = fact?.wind_speed.toString()
                pressure.text = fact?.pressure_mm.toString()
                moisture.text = fact?.humidity.toString()

                if (fact?.temp_water != null) {
                    waterTemperature.text = fact.temp_water.toString()
                    water_view.visibility = View.VISIBLE
                } else {
                    water_view.visibility = View.GONE
                }

                windDirection.setImageResource(
                    resources.getIdentifier(
                        fact?.wind_dir?.getDirection(), "drawable", requireActivity().packageName
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(weatherBroadcastReceiver)
        super.onDestroy()
    }

    inner class WeatherBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (intent.getStringExtra(INTENT_STATUS)) {
                    ON_LOADING -> setWeatherMode(WeatherLoading.Loading)
                    ON_ERROR -> {
                        val error = intent.getStringExtra(WEATHER_ERROR)
                        setWeatherMode(WeatherLoading.Error(error ?: ""))
                    }
                    ON_SUCCESS -> {
                        val weatherString = intent.getStringExtra(WEATHER_DATA)
                        try {
                            val weatherData: WeatherData = Gson().fromJson(
                                weatherString, WeatherData::class.java
                            )
                            setWeatherMode(WeatherLoading.Success(weatherData))
                        } catch (e: Exception) {
                            setWeatherMode(WeatherLoading.Error(e.toString()))
                        }

                    }
                }

            }
        }
    }
}