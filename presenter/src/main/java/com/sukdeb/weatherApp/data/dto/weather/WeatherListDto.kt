package com.sukdeb.weatherApp.data.dto.weather

import com.sukdeb.weatherApp.domain.dataModels.weather.Weather

data class WeatherListDto(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

fun WeatherListDto.toDomain() : Weather {
    return Weather(
        description = this.description,
        icon = this.icon,
        id = this.id,
        main = this.main
    )
}