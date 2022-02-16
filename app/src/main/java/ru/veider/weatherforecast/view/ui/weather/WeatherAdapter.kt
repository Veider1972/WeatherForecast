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
        lateinit var humidity: TextView
        lateinit var rainFall: TextView
    }

    override fun getView(position: Int, forewecastView: View?, parent: ViewGroup): View {
        var rowView = forewecastView
        if (rowView == null) {
            val inflater = LayoutInflater.from(parent.context)
            val binder = ForecastBinding.inflate(inflater, parent, false)
            rowView = binder.root
            holder = WeatherHolder()
            holder.partName = binder.partName
            holder.partName.text = parts[position].part_name.getValue()
            holder.tempMax = binder.tempMax
            holder.tempMax.text = parts[position].temp_max.toString()
            holder.tempMin = binder.tempMin
            holder.tempMin.text = parts[position].temp_min.toString()
            holder.tempFeelsLike = binder.temperatureFeels
            holder.tempFeelsLike.text = parts[position].feels_like.toString()
            holder.windDirection = binder.windDirection
            binder.windDirection.setImageResource(
                weatherFragment.resources.getIdentifier(
                    parts[position].wind_dir.getDirection(),
                    "drawable",
                    weatherFragment.requireActivity().packageName
                )
            )
            holder.windSpeed = binder.windSpeed
            holder.windSpeed.text = parts[position].wind_speed.toString()
            holder.pressure = binder.pressure
            holder.pressure.text = parts[position].pressure_mm.toString()
            holder.humidity = binder.humidity
            holder.humidity.text = parts[position].humidity.toString()
            holder.rainFall = binder.rainFalls
            holder.rainFall.text = parts[position].prec_mm.toString()
            rowView.setTag(holder)
        } else {
            holder = rowView.tag as WeatherHolder
        }
        return rowView
    }
}