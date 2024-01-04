package com.sukdeb.weatherApp.data.dto.weather

import com.sukdeb.weatherApp.domain.dataModels.weather.Clouds

data class CloudsDto(
    val all: Int
)

fun CloudsDto.toDomain(): Clouds {
    return Clouds(
        all = this.all
    )
}