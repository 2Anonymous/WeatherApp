package com.sukdeb.weatherApp.data.dataSource.repository

import com.sukdeb.weatherApp.data.dataSource.network.DataSource
import com.sukdeb.weatherApp.data.dto.user.UserDto
import com.sukdeb.weatherApp.domain.repository.WeatherRepository
import com.sukdeb.weatherApp.domain.responseModel.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val dataSource: DataSource):
    WeatherRepository {

        override suspend fun getWeatherReport(lat: String, lon: String, unit: String, appId: String): Resource<UserDto> =
            dataSource.getWeatherReport(lat, lon, unit, appId)
}