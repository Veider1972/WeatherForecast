package ru.veider.weatherforecast.repository.history

import androidx.room.Room
import ru.gb.kotlinapp.model.room.HistoryDataBase
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.repository.entity.HistoryEntity
import ru.veider.weatherforecast.repository.weather.*

private const val DB_NAME = "History.db"

class HistoryRepositoryImpl : HistoryRepository {

    private lateinit var db: HistoryDataBase

    init {
        if (WeatherApplication.getInstance() != null) {
            db = Room.databaseBuilder(WeatherApplication.getInstance()!!.applicationContext,
                                      HistoryDataBase::class.java,
                                      DB_NAME)
                    .allowMainThreadQueries()
                    .build()
        }
    }

    override fun getHistory(): List<HistoryData> {
        val historyEntities: List<HistoryEntity> = db.historyDao()
                .getHistory()
        val historyData = ArrayList<HistoryData>()
        for (historyEntity in historyEntities) {
            with(historyEntity) {
                historyData.add(HistoryData(now_time,
                                            district_name,
                                            enumValues<Condition>()[condition],
                                            enumValues<DayTime>()[daytime],
                                            temp))
            }
        }
        return historyData
    }

    override fun addHistory(weatherData: WeatherData) {
        with(weatherData) {
            val historyEntity = HistoryEntity(0,
                                              now,
                                              geo_object.locality?.name ?: geo_object.province.name,
                                              fact.temp,
                                              fact.condition.ordinal,
                                              fact.daytime.ordinal)
            db.historyDao()
                    .addHistory(historyEntity)
        }
    }
}