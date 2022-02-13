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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.databinding.CitiesFragmentBinding
import ru.veider.weatherforecast.utils.REQUEST_PERMISSION_LOCATION
import ru.veider.weatherforecast.utils.Utils
import ru.veider.weatherforecast.view.ui.weather.WeatherFragment
import ru.veider.weatherforecast.viewmodel.CitiesLoading
import ru.veider.weatherforecast.viewmodel.WeatherViewModel

class CitiesFragment : Fragment(), Utils, CitiesAdapter.OnCitySelected {

    private var _binder: CitiesFragmentBinding? = null
    private val binder get() = _binder!!
    private lateinit var viewModel: WeatherViewModel
    private lateinit var locationManager: LocationManager
    private var myWeatherQuery: WeatherQuery = WeatherQuery("", 0.0, 0.0, "ru_RU")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binder = CitiesFragmentBinding.inflate(inflater)
        val citiesView = binder.root
        val fab = binder.actionButton
        fab.setOnClickListener {
            viewModel.getCitiesFromRemoteSource()
        }
        binder.myPlace.setOnClickListener {
            chooseCity(myWeatherQuery)
        }
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return citiesView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        val citiesData = Observer<CitiesLoading> { getCitiesData(it) }
        viewModel.getCitiesData().observe(this.viewLifecycleOwner, citiesData)
        viewModel.getCitiesFromRemoteSource()
        val recyclerView = binder.citiesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun getCitiesData(citiesLoading: CitiesLoading) {
        when (citiesLoading) {
            is CitiesLoading.Success -> {
                binder.loadingLayout.visibility = View.GONE
                binder.citiesView.visibility = View.VISIBLE
                binder.citiesRecyclerView.adapter = CitiesAdapter(citiesLoading.citiesData, this)
            }
            is CitiesLoading.Error -> {
                binder.citiesView.visibility = View.GONE
                binder.loadingLayout.visibility = View.GONE
                Snackbar.make(
                    binder.citiesView, getString(R.string.error), Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.reload)) {
                    viewModel.getCitiesData()
                }.show()
            }
            is CitiesLoading.Loading -> {
                binder.citiesView.visibility = View.GONE
                binder.loadingLayout.visibility = View.VISIBLE
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
            Toast.makeText(
                this.requireContext(),
                "Для работы геолокации нужно дать приложению разрешение",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 1000 * 2, 5000f, locationListener
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 1000 * 2, 5000f, locationListener
        )
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
                "ru_RU"
            )
            binder.myPlaceCity.text = myWeatherQuery.name
            binder.myPlaceCoordinates.text = String.format(
                getString(R.string.coordinates),
                doubleToXString(myWeatherQuery.latitude),
                doubleToYString(myWeatherQuery.longitude)
            )
            binder.myPlace.visibility = View.VISIBLE
        }

        override fun onProviderDisabled(provider: String) {
            binder.myPlace.visibility = View.GONE
        }

        override fun onProviderEnabled(provider: String) {
            binder.myPlace.visibility = View.VISIBLE
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            if (provider == LocationManager.GPS_PROVIDER) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.coordinates_by_gps),
                    Toast.LENGTH_LONG
                ).show()
            } else if (provider == LocationManager.NETWORK_PROVIDER) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.coordinates_by_network),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.requireContext(), "Permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun chooseCity(weatherQuery: WeatherQuery) {
        val weatherFragment = WeatherFragment()
        val bundle = Bundle()
        bundle.putParcelable("weather", weatherQuery)
        weatherFragment.arguments = bundle
        parentFragmentManager.beginTransaction().replace(R.id.container, weatherFragment)
            .addToBackStack("weather").commit()
    }
}

