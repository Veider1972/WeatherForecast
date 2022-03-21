package ru.veider.weatherforecast.repository

import android.app.IntentService
import android.content.Intent
import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.veider.weatherforecast.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val INTENT_WEATHER = "INTENT_WEATHER"
const val INTENT_STATUS = "INTENT_STATUS"
const val ON_LOADING = "ON_LOADING"
const val ON_ERROR = "ON_ERROR"
const val ON_SUCCESS = "ON_SUCCESS"
const val WEATHER_DATA = "WEATHER_DATA"
const val WEATHER_ERROR = "WEATHER_ERROR"
private const val ACTION_WEATHER = "GET_WEATHER"
private const val LON = "LONGITUDE"
private const val LAT = "LATITUDE"


class WeatherIntentService : IntentService("WeatherIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_WEATHER -> {
                val lat = intent.getDoubleExtra(LAT, 0.0)
                val lon = intent.getDoubleExtra(LON, 0.0)
                handleActionWeather(lat, lon)
            }
        }
    }

    private fun setOnLoading() {
        val intent = Intent(INTENT_WEATHER).apply {
            putExtra(INTENT_STATUS, ON_LOADING)
            addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        }

        LocalBroadcastManager.getInstance(this.applicationContext)
            .sendBroadcast(intent)
    }

    private fun setOnSuccess(weatherString: String) {
        val intent = Intent(INTENT_WEATHER).apply {
            putExtra(INTENT_STATUS, ON_SUCCESS)
            putExtra(WEATHER_DATA, weatherString)
            addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        }
        LocalBroadcastManager.getInstance(this.applicationContext)
            .sendBroadcast(intent)
    }

    private fun setOnError(e: String) {
        val intent = Intent(INTENT_WEATHER).apply {
            putExtra(INTENT_STATUS, ON_ERROR)
            putExtra(WEATHER_ERROR, e)
            addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        }
        LocalBroadcastManager.getInstance(this.applicationContext)
            .sendBroadcast(intent)
    }


    private fun handleActionWeather(lat: Double?, lon: Double?) {
        val uri = URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}&limit=1&hours=false&extra=false")
        setOnLoading()
        lateinit var urlConnection: HttpsURLConnection
        try {
            urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                requestMethod = "GET"
                readTimeout = 10000
                addRequestProperty("X-Yandex-API-Key", BuildConfig.YANDEX_API_KEY)
            }
            val response = getLines(BufferedReader(InputStreamReader(urlConnection.inputStream)))
            setOnSuccess(response)
        } catch (e: Exception) {
            setOnError(e.toString())
        } finally {
            urlConnection.disconnect()
        }
    }

    //@RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines()
            .collect(Collectors.joining("\n"))
    }

    companion object {
        @JvmStatic
        fun startWeatherService(
            context: Context, lat: Double, lon: Double
        ) {
            val intent = Intent(context, WeatherIntentService::class.java).apply {
                action = ACTION_WEATHER
                putExtra(LAT, lat)
                putExtra(LON, lon)
            }
            context.startService(intent)
        }
    }
}