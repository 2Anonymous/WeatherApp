package com.sukdeb.weatherApp.data.dto.weather

import com.sukdeb.weatherApp.domain.dataModels.weather.Sys

data class SysDto(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)

fun SysDto.toDomain(): Sys {
    return Sys(
        country = this.country,
        sunrise = this.sunrise,
        sunset = this.sunset,
        type = this.type
    )
}