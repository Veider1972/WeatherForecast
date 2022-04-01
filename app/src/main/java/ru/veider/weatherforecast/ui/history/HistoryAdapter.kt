package ru.veider.weatherforecast.ui.history

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.WeatherApplication
import ru.veider.weatherforecast.databinding.HistoryItemBinding
import ru.veider.weatherforecast.repository.history.HistoryData
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
        private val historyFragment: HistoryFragment,
                    ) : RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    var history: List<HistoryData> = arrayListOf()

    fun setData(history: List<HistoryData>) {
        this.history = history
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
                                   ): HistoryHolder {
        return HistoryHolder(LayoutInflater.from(parent.context)
                                     .inflate(R.layout.history_item,
                                              parent,
                                              false) as View)
    }

    override fun onBindViewHolder(
            historyHolder: HistoryHolder,
            position: Int,
                                 ) {
        historyHolder.onBind(history[position])
    }

    override fun getItemCount() = history.size

    inner class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun onBind(historyData: HistoryData) {
            with(HistoryItemBinding.bind(itemView)) {
                place.text = historyData.district_name
                val time = historyData.now * 1000L
                val date = Date(time)
                val format = SimpleDateFormat("DD MMM yyyy HH:mm:ss")
                format.setTimeZone(TimeZone.getTimeZone("GMT"))
                dateTime.text = format.format(date)
                conditions.setImageResource(historyFragment.resources.getIdentifier(historyData.condition.getIcon(historyData.daytime),
                                                                                    "drawable",
                                                                                    historyFragment.requireActivity().packageName))
                temp.text = historyData.temp.toString()
            }
        }
    }
}