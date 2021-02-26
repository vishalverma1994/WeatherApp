package com.weather.data.api

import com.weather.data.model.WeatherData
import com.weather.data.model.WeatherResponse
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun requestWeatherAPI(): Response<WeatherResponse> = apiService.requestWeather()
}