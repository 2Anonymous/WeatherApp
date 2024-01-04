package com.sukdeb.weatherApp.data.dataSource.network

import com.sukdeb.weatherApp.data.dto.user.UserDto
import com.sukdeb.weatherApp.data.dto.weather.WeatherDto
import com.sukdeb.weatherApp.domain.responseModel.Resource
import com.sukdeb.weatherApp.utils.ext.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataSourceImpl @Inject constructor(private val apiService: ApiService) : DataSource {

    override suspend fun getWeatherReport(lat: String, lon: String, unit: String, appId: String): Resource<WeatherDto> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val report = apiService.getWeatherReport(lat, lon, unit, appId)
                Resource.Success(report, "")
            }
        }
    }
}