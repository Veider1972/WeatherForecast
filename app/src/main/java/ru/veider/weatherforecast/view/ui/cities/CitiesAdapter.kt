package ru.veider.weatherforecast.view.ui.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.databinding.CityBinding
import ru.veider.weatherforecast.utils.Utils

class CitiesAdapter(
    private var cities: Array<WeatherQuery>, var onCitySelected: OnCitySelected
) : RecyclerView.Adapter<CitiesAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binder = CityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityHolder(binder.root)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.onBind(
            WeatherQuery(
                cities[position].name,
                cities[position].latitude,
                cities[position].longitude,
                "ru_RU"
            )
        )
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    interface OnCitySelected {
        fun chooseCity(weatherQuery: WeatherQuery)
    }

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Utils {
        private lateinit var weatherQuery: WeatherQuery
        private var binder = CityBinding.bind(itemView)
        private var city = binder.city
        private var coordinates = binder.cityCoordinates

        init {
            itemView.setOnClickListener {
                onCitySelected.chooseCity(weatherQuery)
            }
        }

        fun onBind(weatherQuery: WeatherQuery) {
            this.weatherQuery = weatherQuery
            city.text = weatherQuery.name
            coordinates.text = String.format(
                itemView.context.getString(R.string.coordinates),
                doubleToXString(weatherQuery.latitude),
                doubleToYString(weatherQuery.longitude)
            )
        }
    }
}