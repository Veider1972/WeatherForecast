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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.weather_fragment.*
import okhttp3.*
import ru.veider.weatherforecast.BuildConfig
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.DayTime
import ru.veider.weatherforecast.data.WeatherData
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.databinding.WeatherFragmentBinding
import ru.veider.weatherforecast.utils.showSnack
import ru.veider.weatherforecast.utils.showToast
import ru.veider.weatherforecast.utils.toLatString
import ru.veider.weatherforecast.utils.toLonString
import ru.veider.weatherforecast.repository.WeatherLoadingState
import java.io.IOException
import java.lang.Exception


private const val REQUEST_API_KEY = "X-Yandex-API-Key"

class WeatherFragment : Fragment() {

    private var _binder: WeatherFragmentBinding? = null
    private val binder get() = _binder!!
    private val handle = Handler(Looper.getMainLooper())
    private lateinit var weatherQuery: WeatherQuery

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            val bundle = arguments
            weatherQuery = bundle?.getParcelable<WeatherQuery>("weather") as WeatherQuery
        }
        _binder = WeatherFragmentBinding.inflate(inflater)
        getWeather()
        return binder.root
    }

    //@RequiresApi(value = 24)
    private fun getWeather() {
        setWeatherMode(WeatherLoadingState.LoadingState)
        val client = OkHttpClient()
        val builder: Request.Builder = Request.Builder()
            .apply {
                header(REQUEST_API_KEY, BuildConfig.YANDEX_API_KEY)
                url("https://api.weather.yandex.ru/v2/forecast?lat=${weatherQuery.latitude}&lon=${weatherQuery.longitude}&limit=1&hours=false&extra=false")
            }
        val request: Request = builder.build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                setWeatherMode(WeatherLoadingState.Error(e.toString()))
                requireContext().showToast(getString(R.string.check_internet))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    //setWeatherMode(WeatherLoadingState.Error(response.))
                    requireContext().showToast(getString(R.string.no_answer))
                } else {
                    try {
                        val answer = response.body?.charStream()
                        val weatherData: WeatherData = Gson().fromJson(
                            answer, WeatherData::class.java
                        )
                        setWeatherMode(WeatherLoadingState.Success(weatherData))
                    } catch (e: Exception) {
                        setWeatherMode(WeatherLoadingState.Error(e.toString()))
                        requireContext().showToast(getString(R.string.error) + ": " + e.toString())
                    }
                }
            }
        })
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
                                getWeather()
                            })
                    }
                    is WeatherLoadingState.LoadingState -> {
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
}