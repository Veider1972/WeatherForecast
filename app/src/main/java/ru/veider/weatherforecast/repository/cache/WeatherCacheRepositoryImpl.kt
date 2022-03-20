package ru.veider.weatherforecast.repository.cache

import androidx.room.Room
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.repository.entity.WeatherEntity
import ru.veider.weatherforecast.repository.getCleanedTime
import ru.veider.weatherforecast.repository.weather.*

private const val DB_NAME = "WeatherCache.db"

class WeatherCacheRepositoryImpl : WeatherCacheRepository {

    private lateinit var db: WeatherDataBase

    init {
        if (WeatherApplication.getInstance() != null) {
            db = Room.databaseBuilder(WeatherApplication.getInstance()!!.applicationContext,
                                      WeatherDataBase::class.java,
                                      DB_NAME)
                    .allowMainThreadQueries()
                    .build()
        }
    }

    override fun cleanCache() {
        db.weatherCacheDao()
                .cleanCache(getCleanedTime())
    }

    override fun getCache(
            latitude: Double,
            longitude: Double,
                         ): WeatherData {
        val weatherEntities: List<WeatherEntity> = db.weatherCacheDao()
                .getCache(latitude,
                          longitude,
                          getCleanedTime())
        val weatherEntity = weatherEntities[weatherEntities.size - 1]
        return WeatherData(weatherEntity.now_time,
                           Info(weatherEntity.latitude,
                                weatherEntity.longitude),
                           GeoObject(District(weatherEntity.district_id,
                                              weatherEntity.district_name),
                                     Locality(weatherEntity.locality_id,
                                              weatherEntity.locality_name),
                                     Province(weatherEntity.province_id,
                                              weatherEntity.province_name),
                                     Country(weatherEntity.country_id,
                                             weatherEntity.country_name)),
                           Fact(weatherEntity.temp,
                                weatherEntity.feels_like,
                                weatherEntity.temp_water,
                                enumValues<Condition>()[weatherEntity.condition],
                                weatherEntity.wind_speed,
                                enumValues<WindDir>()[weatherEntity.wind_dir],
                                weatherEntity.pressure_mm,
                                weatherEntity.humidity,
                                enumValues<DayTime>()[weatherEntity.daytime]))
    }

    override fun checkCache(
            latitude: Double,
            longitude: Double,
                           ): Boolean {
        val state = db.weatherCacheDao()
                .checkCache(latitude,
                            longitude,
                            getCleanedTime())
        return state > 0
    }

    override fun addCache(weatherData: WeatherData) {
        with(weatherData) {
            val weatherEntity = WeatherEntity(1,
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
            db.weatherCacheDao()
                    .addCache(weatherEntity)
        }
    }
}