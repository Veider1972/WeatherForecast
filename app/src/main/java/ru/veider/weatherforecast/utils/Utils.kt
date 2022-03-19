package ru.veider.weatherforecast.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.math.abs
import kotlin.math.truncate

const val REQUEST_PERMISSION_LOCATION = 99
const val CITIES_KEY = "CITIES_KEY"

const val deltaTime : Int = 30 // Время хранения кэша погоды в минутах
fun getCleanedTime():Long{
    return System.currentTimeMillis()/1000L - deltaTime*60
}

fun View.showSnack(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .setAction(actionText, action)
        .show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG)
        .show()
}

fun Double.toLatString(): String {
    val degree = truncate(this).toInt()
    val minutes = truncate((abs(this) - abs(degree)) * 60).toInt()
    val seconds = ((abs(this) - abs(degree) - minutes / 60) * 60)
    return String.format("%+03d°%02d'%05.2f\"", degree, minutes, seconds)
}

fun Double.toLonString(): String {
    val degree = truncate(this).toInt()
    val minutes = truncate((abs(this) - abs(degree)) * 60).toInt()
    val seconds = ((abs(this) - abs(degree) - minutes / 60) * 60)
    return String.format("%+04d°%02d'%05.2f\"", degree, minutes, seconds)
}

const val TAG = "TAG"
