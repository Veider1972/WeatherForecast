package ru.veider.weatherforecast.utils

import kotlin.math.abs
import kotlin.math.truncate

const val REQUEST_PERMISSION_LOCATION = 99

interface Utils {
    fun doubleToXString(value: Double): String {
        val degree = truncate(value).toInt()
        val minutes = truncate((abs(value) - abs(degree)) * 60).toInt()
        val seconds = ((abs(value) - abs(degree) - minutes / 60) * 60)
        return String.format("%+03d°%02d'%05.2f\"", degree, minutes, seconds)
    }

    fun doubleToYString(value: Double): String {
        val degree = truncate(value).toInt()
        val minutes = truncate((abs(value) - abs(degree)) * 60).toInt()
        val seconds = ((abs(value) - abs(degree) - minutes / 60) * 60)
        return String.format("%+04d°%02d'%05.2f\"", degree, minutes, seconds)
    }
}