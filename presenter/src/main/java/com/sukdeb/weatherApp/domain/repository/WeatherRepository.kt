package com.sukdeb.weatherApp.domain.repository

import com.sukdeb.weatherApp.data.dto.user.UserDto
import com.sukdeb.weatherApp.domain.responseModel.Resource

interface WeatherRepository {

    suspend fun getWeatherReport(lat: String, lon: String, unit: String, appId: String): Resource<UserDto>
}