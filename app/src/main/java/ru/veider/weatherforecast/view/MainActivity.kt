package ru.veider.weatherforecast.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.databinding.MainActivityBinding
import ru.veider.weatherforecast.utils.REQUEST_PERMISSION_LOCATION
import ru.veider.weatherforecast.view.ui.cities.CitiesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binder: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissionForLocation()

        binder = MainActivityBinding.inflate(layoutInflater)
        setContentView(binder.root)
        savedInstanceState?.let {}
            .run {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CitiesFragment())
                    .commitNow()
            }
    }

    private fun checkPermissionForLocation(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) true
            else {
                // Show the permission request
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else true
    }
}