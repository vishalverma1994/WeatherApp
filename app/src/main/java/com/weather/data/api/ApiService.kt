package com.weather.data.api

import com.weather.data.model.WeatherResponse
import com.weather.utils.API_WEATHER_LIST
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(API_WEATHER_LIST)
    suspend fun requestWeather(): Response<WeatherResponse>
}