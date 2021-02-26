package com.weather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.data.model.WeatherData
import com.weather.data.repository.MainRepository
import com.weather.utils.NO_INTERNET_CONNECTION_MESSAGE
import com.weather.utils.NetworkHelper
import com.weather.utils.Resource
import kotlinx.coroutines.launch


class MainViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    val _weatherListResponse = MutableLiveData<Resource<List<WeatherData>>>()

    fun requestWeatherAPI() {
        viewModelScope.launch {
            _weatherListResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.requestWeatherAPI()
                    .let { response ->
                        if (response.isSuccessful && response.body() != null) {
                            response.body()?.let { weatherResponse ->
                                _weatherListResponse.postValue(Resource.success(weatherResponse.weatherData))
                            }
                        } else _weatherListResponse.postValue(
                            Resource.error(
                                response.body().toString(),
                                null
                            )
                        )
                    }
            } else _weatherListResponse.postValue(
                Resource.error(
                    NO_INTERNET_CONNECTION_MESSAGE,
                    null
                )
            )
        }
    }

}