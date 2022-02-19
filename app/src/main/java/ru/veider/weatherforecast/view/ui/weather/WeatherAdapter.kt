package ru.veider.weatherforecast.view.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.Parts
import ru.veider.weatherforecast.databinding.ForecastBinding

class WeatherAdapter(
    private val weatherFragment: WeatherFragment, private var parts: Array<Parts>
) : ArrayAdapter<Parts>(weatherFragment.requireContext(), R.layout.forecast, parts) {

    private lateinit var holder: WeatherHolder

    inner class WeatherHolder {
        lateinit var partName: TextView
        lateinit var tempMax: TextView
        lateinit var tempMin: TextView
        lateinit var tempFeelsLike: TextView
        lateinit var windDirection: ImageView
        lateinit var windSpeed: TextView
        lateinit var pressure: TextView
        lateinit var moisture: TextView
        lateinit var rainFall: TextView
    }

    override fun getView(position: Int, forewecastView: View?, parent: ViewGroup): View {
        var rowView = forewecastView
        if (rowView == null) {
            val inflater = LayoutInflater.from(parent.context)
            val binder = ForecastBinding.inflate(inflater, parent, false)
            rowView = binder.root
            holder = WeatherHolder().also {
                with(binder) {
                    with(parts[position]) {
                        it.partName = partName.also { it.text = part_name.getValue() }
                        it.tempMax = tempMax.also { it.text = temp_max.toString() }
                        it.tempMin = tempMin.also { it.text = temp_min.toString() }
                        it.tempFeelsLike = temperatureFeels.also { it.text = feels_like.toString() }
                        it.windDirection = windDirection.also {
                            it.setImageResource(
                                weatherFragment.resources.getIdentifier(
                                    wind_dir.getDirection(),
                                    "drawable",
                                    weatherFragment.requireActivity().packageName
                                )
                            )
                        }
                        it.windSpeed = windSpeed.also { it.text = wind_speed.toString() }
                        it.pressure = pressure.also { it.text = pressure_mm.toString() }
                        it.moisture = moisture.also { it.text = humidity.toString() }
                        it.rainFall = rainFalls.also { it.text = prec_mm.toString() }
                    }
                }
            }
            rowView.setTag(holder)
        } else holder = rowView.tag as WeatherHolder
        return rowView
    }
}