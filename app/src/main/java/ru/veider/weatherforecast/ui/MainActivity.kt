package ru.veider.weatherforecast.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.databinding.MainActivityBinding
import ru.veider.weatherforecast.repository.weather.Language
import ru.veider.weatherforecast.repository.weather.WeatherQuery
import ru.veider.weatherforecast.ui.cities.CitiesFragment
import ru.veider.weatherforecast.ui.history.HistoryFragment
import ru.veider.weatherforecast.ui.map.MapFragment
import ru.veider.weatherforecast.ui.utils.REQUEST_PERMISSION_LOCATION
import ru.veider.weatherforecast.ui.weather.WeatherFragment
import ru.veider.weatherforecast.utils.*
import android.content.BroadcastReceiver as BroadcastReceiver


class MainActivity : AppCompatActivity() {

    private lateinit var binder: MainActivityBinding

    private var broadcastReceiver = WeatherBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissionForLocation()

        binder = MainActivityBinding.inflate(layoutInflater)
        setContentView(binder.root)
        savedInstanceState?.let {}.run {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CitiesFragment.newInstance()).commitNow()
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(BROADCAST_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HistoryFragment.newInstance())
                    .addToBackStack("history").commit()
                true
            }
            R.id.menu_map -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container, MapFragment.newInstance(
                        CitiesFragment.newInstance().myWeatherQuery
                                                           )
                                                                 ).addToBackStack("map").commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermissionForLocation(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                                                 ) == PackageManager.PERMISSION_GRANTED
            ) true
            else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                                                 )
                false
            }
        } else true
    }

    inner class WeatherBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message: String =
                intent?.getStringExtra(MESSAGE) ?: resources.getString(R.string.attention)
            val latitude: Double = intent?.getDoubleExtra(LATITUDE, 1000.0) ?: 1000.0
            val longitude: Double = intent?.getDoubleExtra(LONGITUDE, 1000.0) ?: 1000.0
            val weatherFragment = WeatherFragment.newInstance().apply {
                if (latitude != 1000.0 && longitude != 1000.0) {
                    arguments = Bundle().apply {
                        putParcelable(
                            "weather", WeatherQuery(message, latitude, longitude, Language.RU)
                                     )
                    }
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.container, weatherFragment)
                .addToBackStack("weather").commit()
        }
    }
}