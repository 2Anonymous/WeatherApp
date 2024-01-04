package com.sukdeb.weatherApp.domain.useCase

import com.sukdeb.weatherApp.domain.dataModels.weather.WeatherModel
import com.sukdeb.weatherApp.domain.responseModel.Resource

interface WeatherUseCase {
    suspend fun getWeatherReport(lat: String, lon: String, unit: String, appId: String): Resource<WeatherModel>
}