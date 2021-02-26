package com.weather.data.repository

import com.weather.data.api.ApiHelper


class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun requestWeatherAPI() = apiHelper.requestWeatherAPI()

}