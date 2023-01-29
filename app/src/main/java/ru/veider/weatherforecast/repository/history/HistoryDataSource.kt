package ru.veider.weatherforecast.repository.history

import androidx.room.Room
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.repository.entity.HistoryEntity
import ru.veider.weatherforecast.repository.room.WeatherDataBase
import ru.veider.weatherforecast.repository.weather.Condition
import ru.veider.weatherforecast.repository.weather.DayTime
import ru.veider.weatherforecast.repository.weather.WeatherData
import ru.veider.weatherforecast.utils.DB_NAME
import ru.veider.weatherforecast.utils.PRIMARY_KEY

class HistoryDataSource(private val historyEvents: HistoryEvents) {
    private lateinit var db: WeatherDataBase

    interface HistoryEvents {
        fun getHistoryData(historyData: List<HistoryData>)
    }

    companion object {
        @JvmStatic
        fun getInstance(historyEvents: HistoryEvents) = HistoryDataSource(historyEvents)
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
    fun getHistory() {
        GlobalScope.launch {
            val historyEntities: List<HistoryEntity> = db.historyDao().getHistory()
            val historyData: List<HistoryData> = historyEntities.map {
                HistoryData(
                    it.now_time,
                    it.district_name,
                    enumValues<Condition>()[it.condition],
                    enumValues<DayTime>()[it.daytime],
                    it.temp)
            }
            historyEvents.getHistoryData(historyData)
        }
    }

    @DelicateCoroutinesApi
    fun addHistory(weatherData: WeatherData) {
        GlobalScope.launch {
            with(weatherData) {
                val historyEntity = HistoryEntity(
                    PRIMARY_KEY,
                    now,
                    geo_object.locality?.name ?: geo_object.province.name,
                    fact.temp,
                    fact.condition.ordinal,
                    fact.daytime.ordinal)
                db.historyDao().addHistory(historyEntity)
            }
        }
    }
}