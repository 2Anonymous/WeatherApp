package com.sukdeb.weatherApp.data.dto.weather

import com.sukdeb.weatherApp.domain.dataModels.weather.Coord

data class CoordDto(
    val lat: Double,
    val lon: Double
)

fun CoordDto.toDomain(): Coord {
    return Coord(
        lat = this.lat,
        lon = this.lon
    )
}