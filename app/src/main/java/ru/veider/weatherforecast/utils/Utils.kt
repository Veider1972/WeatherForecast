package ru.veider.weatherforecast.utils

const val deltaTime: Int = 30 // Время хранения кэша погоды в минутах
fun getCleanedTime(): Long {
    return System.currentTimeMillis() / 1000L - deltaTime * 60
}