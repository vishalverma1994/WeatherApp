package com.weather.data.api

import com.weather.data.model.WeatherData
import com.weather.data.model.WeatherResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun requestWeatherAPI(): Response<WeatherResponse>

}