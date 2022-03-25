package ru.veider.weatherforecast.view

import android.app.Application

class WeatherApplication:Application() {

    companion object {
        private var application : Application? = null
        fun getInstance()  = application
    }

    override fun onCreate() {
        application = this
        super.onCreate()
    }
}