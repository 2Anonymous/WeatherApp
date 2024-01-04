package com.sukdeb.weatherApp.data.dataSource.network

import com.sukdeb.weatherApp.data.dto.user.UserDto
import com.sukdeb.weatherApp.data.dto.weather.WeatherDto
import com.sukdeb.weatherApp.domain.responseModel.Resource

interface DataSource {

    suspend fun getWeatherReport(
        lat: String,
        lon: String,
        unit: String,
        appId: String
    ): Resource<WeatherDto>

}