package ru.veider.weatherforecast.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.databinding.MainActivityBinding
import ru.veider.weatherforecast.ui.cities.CitiesFragment
import ru.veider.weatherforecast.ui.history.HistoryFragment
import ru.veider.weatherforecast.ui.utils.REQUEST_PERMISSION_LOCATION

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
                            .replace(R.id.container,
                                     CitiesFragment.newInstance())
                            .commitNow()
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu,
                             menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container,
                                 HistoryFragment.newInstance())
                        .addToBackStack("history")
                        .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermissionForLocation(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.applicationContext,
                                                  Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) true
            else { // Show the permission request
                ActivityCompat.requestPermissions(this,
                                                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                  REQUEST_PERMISSION_LOCATION)
                false
            }
        } else true
    }
}