package ru.veider.weatherforecast

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class WeatherApplication : Application() {

    companion object {
        private var application: Application? = null
        fun getInstance() = application
    }

    override fun onCreate() {
        application = this
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPKIT_KEY)
        super.onCreate()
    }
}