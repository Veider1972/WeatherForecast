package ru.veider.weatherforecast.view.ui.cities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.DataLoading
import ru.veider.weatherforecast.data.Language
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.databinding.CitiesFragmentBinding
import ru.veider.weatherforecast.utils.*
import ru.veider.weatherforecast.view.ui.weather.WeatherFragment
import ru.veider.weatherforecast.repository.CitiesLoadingState
import ru.veider.weatherforecast.viewmodel.CitiesViewModel

class CitiesFragment : Fragment(),
    CitiesAdapter.OnCitySelected {

    private var _binder: CitiesFragmentBinding? = null
    private val binder get() = _binder!!
    private val viewModel: CitiesViewModel by lazy { ViewModelProvider(this)[CitiesViewModel::class.java] }
    private val locationManager: LocationManager by lazy { requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private var myWeatherQuery: WeatherQuery = WeatherQuery(
        "", 0.0, 0.0, Language.RU
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binder = CitiesFragmentBinding.inflate(inflater)
        with(binder) {
            actionButton.setOnClickListener {
                when (viewModel.dataLoading) {
                    DataLoading.RUSSIAN -> viewModel.dataLoading = DataLoading.FOREIGN
                    DataLoading.FOREIGN -> viewModel.dataLoading = DataLoading.RUSSIAN
                }
                viewModel.getCitiesFromRemoteSource()
            }
            myPlace.setOnClickListener { chooseCity(myWeatherQuery) }
        }
        with(viewModel) {
            getCitiesData().observe(this@CitiesFragment.viewLifecycleOwner) { getCitiesData(it) }
            getCitiesFromRemoteSource()
        }
        binder.citiesRecyclerView.layoutManager = LinearLayoutManager(this.context)
        return binder.root
    }

    private fun getCitiesData(citiesLoadingState: CitiesLoadingState) {
        with(binder) {
            when (citiesLoadingState) {
                is CitiesLoadingState.Success -> {
                    loadingLayout.visibility = View.GONE
                    citiesView.visibility = View.VISIBLE
                    citiesRecyclerView.adapter = CitiesAdapter(
                        citiesLoadingState.citiesData, this@CitiesFragment
                    )
                }
                is CitiesLoadingState.Error -> {
                    citiesView.visibility = View.GONE
                    loadingLayout.visibility = View.GONE
                    this@CitiesFragment.view?.showSnack(getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getCitiesData() })
                }
                is CitiesLoadingState.LoadingState -> {
                    citiesView.visibility = View.GONE
                    loadingLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requireContext().showToast(getString(R.string.geolocation_permission))
            return
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000 * 2, 5000f, locationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 2, 5000f, locationListener
            )
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
        _binder = null
    }

    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            myWeatherQuery = WeatherQuery(
                getString(R.string.my_place),
                location.latitude,
                location.longitude,
                Language.valueOf(
                    getString(R.string.default_location_language)
                )
            ).also {
                with(binder) {
                    myPlaceCity.text = it.name
                    myPlaceCoordinates.text = String.format(
                        getString(R.string.coordinates),
                        it.latitude.toLatString(),
                        it.latitude.toLonString()
                    )
                    myPlace.visibility = View.VISIBLE
                }
            }
        }

        override fun onProviderDisabled(provider: String) {
            binder.myPlace.visibility = View.GONE
        }

        override fun onProviderEnabled(provider: String) {
            binder.myPlace.visibility = View.VISIBLE
        }

        override fun onStatusChanged(
            provider: String, status: Int, extras: Bundle
        ) {
            when (provider) {
                LocationManager.GPS_PROVIDER -> requireContext().showToast(getString(R.string.coordinates_by_gps))
                LocationManager.NETWORK_PROVIDER -> requireContext().showToast(getString(R.string.coordinates_by_network))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requireContext().showToast(getString(R.string.permission_granted))
            }
        }
    }

    override fun chooseCity(weatherQuery: WeatherQuery) {
        val weatherFragment = WeatherFragment().apply {
            arguments = Bundle().apply {
                putParcelable("weather", weatherQuery)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, weatherFragment)
            .addToBackStack("weather")
            .commit()
    }
}

