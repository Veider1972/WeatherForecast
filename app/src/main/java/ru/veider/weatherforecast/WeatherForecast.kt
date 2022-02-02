package ru.veider.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.veider.weatherforecast.databinding.ActivityWeatherForecastBinding
import kotlin.random.Random

class WeatherForecast : AppCompatActivity() {

    private var cityes: Array<String> = arrayOf("Москва", "Санкт-Петербург", "Вологда")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binder = ActivityWeatherForecastBinding.inflate(layoutInflater)

        setContentView(binder.root)

        val button = binder.button
        val textDataClassResult = binder.dataClassResult
        val textCopyResult = binder.objectCopyResult
        val textCycleResult = binder.cycleResult
        button.setOnClickListener {

            val weatherObj = Weather(cityes[Random.nextInt(0, 2)],Random.nextInt(-25, 30))

            textDataClassResult.text = weatherObj.toString()

            val weatherObj2 = weatherObj.copy(cityes[0])

            textCopyResult.text = weatherObj2.toString()

            var intValue =1

            for (i in 1..10)
                intValue *= i

            textCycleResult.text = String.format("Произведение чисел от 1 до 10 = %d",intValue)
        }

    }

    data class Weather(val city: String, val temperature: Int) {
        override fun toString(): String {
            return String.format("в городе %s температура %d°С", city, temperature)
        }
    }


}