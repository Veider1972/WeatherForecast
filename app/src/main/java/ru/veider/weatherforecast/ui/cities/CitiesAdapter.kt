package ru.veider.weatherforecast.ui.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.veider.weatherforecast.repository.weather.Language
import ru.veider.weatherforecast.repository.weather.WeatherQuery
import ru.veider.weatherforecast.databinding.CityItemBinding

class CitiesAdapter(
    private var cities: Array<WeatherQuery>, var onCitySelected: OnCitySelected) :
    RecyclerView.Adapter<CitiesAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binder = CityItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return CityHolder(binder.root)
    }

    override fun onBindViewHolder(
        holder: CityHolder, position: Int) {
        holder.onBind(cities[position].apply {
            WeatherQuery(name, latitude, longitude, Language.RU)
        })
    }

    override fun getItemCount() = cities.size

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
            with(CityItemBinding.bind(itemView)) {
                city.text = weatherQuery.name
            }
        }
    }
}