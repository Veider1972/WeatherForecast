package ru.veider.weatherforecast.repository.cache

import androidx.room.Room
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.repository.entity.WeatherEntity
import ru.veider.weatherforecast.utils.getCleanedTime
import ru.veider.weatherforecast.repository.room.WeatherDataBase
import ru.veider.weatherforecast.repository.weather.*
import ru.veider.weatherforecast.utils.DB_NAME
import ru.veider.weatherforecast.utils.PRIMARY_KEY


class WeatherCacheDataSource(val weatherCacheEvent: WeatherCacheEvent) {
    private lateinit var db: WeatherDataBase

    interface WeatherCacheEvent {
        fun getWeatherCache(weatherData: WeatherData)
        fun checkCacheData(latitude: Double, longitude: Double, hasData:Boolean)
    }

    companion object {
        @JvmStatic
        fun getInstance(weatherCacheEvent: WeatherCacheEvent) =
            WeatherCacheDataSource(weatherCacheEvent)
    }

    init {
        if (WeatherApplication.getInstance() != null) {
            db = Room.databaseBuilder(
                WeatherApplication.getInstance()!!.applicationContext,
                WeatherDataBase::class.java,
                DB_NAME).build()
        }
    }

    @DelicateCoroutinesApi
    fun cleanCache() {
        GlobalScope.launch {
            db.weatherCacheDao().cleanCache(getCleanedTime())
        }
    }

    @DelicateCoroutinesApi
    fun getCache(latitude: Double, longitude: Double) {
        GlobalScope.launch {
            val weatherEntities: List<WeatherEntity> =
                db.weatherCacheDao().getCache(latitude, longitude, getCleanedTime())
            val weatherEntity = weatherEntities[weatherEntities.size - 1]
            weatherCacheEvent.getWeatherCache(with(weatherEntity) {
                WeatherData(
                    now_time, Info(latitude, longitude), GeoObject(
                        District(
                            district_id, district_name), Locality(
                            locality_id, locality_name), Province(
                            province_id, province_name), Country(
                            country_id, country_name)), Fact(
                        temp,
                        feels_like,
                        temp_water,
                        enumValues<Condition>()[condition],
                        wind_speed,
                        enumValues<WindDir>()[wind_dir],
                        pressure_mm,
                        humidity,
                        enumValues<DayTime>()[daytime]))
            })
        }
    }

    @DelicateCoroutinesApi
    fun checkCache(latitude: Double, longitude: Double) {
        GlobalScope.launch {
            val state = db.weatherCacheDao().checkCache(latitude, longitude, getCleanedTime())
            weatherCacheEvent.checkCacheData(latitude,  longitude, state > 0)
        }
    }

    @DelicateCoroutinesApi
    fun addCache(weatherData: WeatherData) {
        GlobalScope.launch {
            with(weatherData) {
                val weatherEntity = WeatherEntity(
                    PRIMARY_KEY,
                    now,
                    info.latitude,
                    info.longitude,
                    geo_object.district?.id,
                    geo_object.district?.name,
                    geo_object.locality?.id,
                    geo_object.locality?.name,
                    geo_object.province.id,
                    geo_object.province.name,
                    geo_object.country.id,
                    geo_object.country.name,
                    fact.temp,
                    fact.feels_like,
                    fact.temp_water,
                    fact.condition.ordinal,
                    fact.wind_speed,
                    fact.wind_dir.ordinal,
                    fact.pressure_mm,
                    fact.humidity,
                    fact.daytime.ordinal)
                db.weatherCacheDao().addCache(weatherEntity)
            }
        }
    }
}