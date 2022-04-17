package ru.veider.weatherforecast.utils

const val TITLE = "title"
const val MESSAGE = "message"
const val LATITUDE = "latitude"
const val LONGITUDE = "longitude"
const val CHANNEL_ID = "channel_id"
const val NOTIFICATION_ID = 1
const val BROADCAST_ACTION = "custom-event-name"
const val TAG = "TAG"
const val CITIES_KEY = "CITIES_KEY"
const val DB_NAME = "History.db"
const val PRIMARY_KEY : Long = 0
const val REQUEST_PERMISSION_LOCATION = 99

val vibroPattern = arrayOf(500L, 500L, 500L, 500L, 500L, 500L, 500L, 500L, 500L).toLongArray()