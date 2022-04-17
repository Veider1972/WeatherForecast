package ru.veider.weatherforecast.repository.history

import ru.veider.weatherforecast.repository.weather.Condition
import ru.veider.weatherforecast.repository.weather.DayTime

data class HistoryData(
    val now: Long,
    val district_name: String,
    val condition: Condition,
    val daytime: DayTime,
    val temp: Int)
