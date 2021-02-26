package com.weather.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("data")
    val weatherData: List<WeatherData>
)

data class WeatherData(
    @SerializedName("temp")
    val temperature: Int = 0,
    @SerializedName("time")
    val time: Long = 0,
    @SerializedName("rain")
    val rain: Int = 0,
    @SerializedName("wind")
    val wind: Int = 0,
)