package ru.veider.weatherforecast.ui.weather

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
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.weather_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.veider.weatherforecast.BuildConfig
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.databinding.WeatherFragmentBinding
import ru.veider.weatherforecast.repository.history.HistoryRepositoryImpl
import ru.veider.weatherforecast.repository.weather.WeatherData
import ru.veider.weatherforecast.repository.weather.WeatherLoadingState
import ru.veider.weatherforecast.repository.weather.WeatherQuery
import ru.veider.weatherforecast.ui.utils.showSnack
import ru.veider.weatherforecast.ui.utils.toLatString
import ru.veider.weatherforecast.ui.utils.toLonString
import ru.veider.weatherforecast.viewmodel.WeatherViewModel
import java.util.*


class WeatherFragment : Fragment() {

    private var _binder: WeatherFragmentBinding? = null
    private val binder get() = _binder!!
    private val handle = Handler(Looper.getMainLooper())
    private lateinit var weatherQuery: WeatherQuery
    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }
    private var yandexMap: MapKit? = null

    companion object {
        @JvmStatic
        fun newInstance() = WeatherFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
                             ): View {
        arguments?.let {
            val bundle = arguments
            weatherQuery = bundle?.getParcelable<WeatherQuery>("weather") as WeatherQuery
        }
        _binder = WeatherFragmentBinding.inflate(inflater)
        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner,
                                                 Observer { setWeatherMode(it) })
        weatherViewModel.getWeatherFromRemoteSource(weatherQuery.latitude, weatherQuery.longitude)
        return binder.root
    }


    override fun onStop() {
        yandex_map.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        yandex_map.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setWeatherMode(weatherLoadingState: WeatherLoadingState) {
        handle.post {
            with(binder) {
                when (weatherLoadingState) {
                    is WeatherLoadingState.Success -> {
                        loadingLayout.loadingLayout.visibility = View.GONE
                        weatherView.visibility = View.VISIBLE
                        setData(weatherLoadingState.weatherData)
                    }
                    is WeatherLoadingState.Error -> {
                        weatherView.visibility = View.GONE
                        loadingLayout.loadingLayout.visibility = View.GONE
                        weatherView.showSnack(
                                getString(R.string.error) + ": " + weatherLoadingState.error,
                                getString(R.string.reload), {
                                    weatherViewModel.getWeatherFromRemoteSource(
                                            weatherQuery.latitude, weatherQuery.longitude)
                                })
                    }
                    is WeatherLoadingState.Loading -> {
                        weatherView.visibility = View.GONE
                        loadingLayout.loadingLayout.visibility = View.VISIBLE
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
                GlobalScope.launch {
                    HistoryRepositoryImpl().addHistory(weatherData)
                }
                Picasso.get().load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                        .into(background)
                cityName.text = weatherData.geo_object.locality?.name
                        ?: weatherData.geo_object.province.name
                cityCoordinates.text = String.format(getString(R.string.coordinates),
                                                     weatherData.info.latitude.toLatString(),
                                                     weatherData.info.longitude.toLonString())

                conditions.setImageResource(
                        resources.getIdentifier(fact.condition.getIcon(fact.daytime), "drawable",
                                                requireActivity().packageName))
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
                        resources.getIdentifier(fact.wind_dir.getDirection(), "drawable",
                                                requireActivity().packageName))
                yandexMap.map.apply {
                    val point = Point(weatherData.info.latitude, weatherData.info.longitude)
                    move(CameraPosition(point, 12.0f, 0.0f, 0.0f))
                    mapObjects.addPlacemark(point, ImageProvider.fromResource(requireContext(),
                                                                              R.drawable.map_pin))
                }
            }
        }
    }
}