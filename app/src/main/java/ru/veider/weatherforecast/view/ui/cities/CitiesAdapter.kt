package ru.veider.weatherforecast.view.ui.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.data.WeatherQuery
import ru.veider.weatherforecast.databinding.CityBinding
import ru.veider.weatherforecast.utils.toLatString
import ru.veider.weatherforecast.utils.toLonString

class CitiesAdapter(
    private var cities: Array<WeatherQuery>, var onCitySelected: OnCitySelected
) : RecyclerView.Adapter<CitiesAdapter.CityHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CityHolder {
        val binder = CityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CityHolder(binder.root)
    }

    override fun onBindViewHolder(
        holder: CityHolder, position: Int
    ) {
        holder.onBind(cities[position].apply {
            WeatherQuery(
                name, latitude, longitude, "ru_RU"
            )
        })
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    interface OnCitySelected {
        fun chooseCity(weatherQuery: WeatherQuery)
    }

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var weatherQuery: WeatherQuery

        init {
            itemView.setOnClickListener {
                onCitySelected.chooseCity(weatherQuery)
            }
        }

        fun onBind(weatherQuery: WeatherQuery) {
            this.weatherQuery = weatherQuery
            with(CityBinding.bind(itemView)) {
                city.text = weatherQuery.name
                cityCoordinates.text = String.format(
                    itemView.context.getString(R.string.coordinates),
                    weatherQuery.latitude.toLatString(),
                    weatherQuery.longitude.toLonString()
                )
            }

        }
    }
}