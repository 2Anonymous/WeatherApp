package com.sukdeb.weatherApp.domain.useCase

import com.sukdeb.weatherApp.data.dto.weather.toDomain
import com.sukdeb.weatherApp.domain.dataModels.weather.WeatherModel
import com.sukdeb.weatherApp.domain.repository.WeatherRepository
import com.sukdeb.weatherApp.domain.responseModel.Resource
import javax.inject.Inject

class WeatherUseCaseImpl @Inject constructor(private val weatherRepository: WeatherRepository): WeatherUseCase {

    override suspend fun getWeatherReport(lat: String, lon: String, unit: String, appId: String): Resource<WeatherModel> {
        return  when (val response = weatherRepository.getWeatherReport(lat, lon, unit, appId)){
            is Resource.Loading -> {Resource.Loading()}
            is Resource.Error -> {Resource.Error(null, response.message!!)}
            is Resource.Success -> {
                Resource.Success(response.data?.toDomain(),response.message!!)
            }
        }
    }
}