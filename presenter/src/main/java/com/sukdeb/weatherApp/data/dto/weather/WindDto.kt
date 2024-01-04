package com.sukdeb.weatherApp.data.dto.weather

import com.sukdeb.weatherApp.domain.dataModels.weather.Wind

data class WindDto(
    val deg: Int,
    val gust: Double,
    val speed: Double
)

fun WindDto.toDomain(): Wind {
    return Wind(
        deg = this.deg,
        gust = this.gust,
        speed = this.speed
    )
}