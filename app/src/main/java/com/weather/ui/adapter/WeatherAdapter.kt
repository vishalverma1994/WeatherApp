package com.weather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.weather.R
import com.weather.data.model.WeatherData
import com.weather.databinding.AdapterWeatherBinding

class WeatherAdapter(private val onItemClickListener: (Int) -> Unit) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var weatherList = emptyList<WeatherData>()

    fun setWeatherList(weatherList: List<WeatherData>) {
        this.weatherList = weatherList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_weather,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(weatherList[position])
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class ViewHolder(private val binding: AdapterWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(weatherData: WeatherData) {
            binding.weatherData = weatherData
        }
    }
}