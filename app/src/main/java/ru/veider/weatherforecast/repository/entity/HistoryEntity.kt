package ru.veider.weatherforecast.repository.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val now_time: Long = 0,
        val district_name: String = "",
        val temp: Int = 0,
        val condition: Int = 0,
        val daytime: Int = 0,
                        )