package com.sukdeb.weatherApp.data.dto.weather

import com.sukdeb.weatherApp.domain.dataModels.weather.Weather
import com.sukdeb.weatherApp.domain.dataModels.weather.WeatherModel

data class WeatherDto(
    val base: String,
    val clouds: CloudsDto,
    val cod: Int,
    val coord: CoordDto,
    val dt: Int,
    val id: Int,
    val main: MainDto,
    val name: String,
    val sys: SysDto,
    val timezone: Int,
    val visibility: Int,
    val weather: MutableList<WeatherListDto>,
    val wind: WindDto
)

fun WeatherDto.toDomain(): WeatherModel {
    return WeatherModel(
        base = this.base,
        clouds = this.clouds.toDomain(),
        cod = this.cod,
        coord = this.coord.toDomain(),
        dt = this.dt,
        id = this.id,
        main = this.main.toDomain(),
        name = this.name,
        sys = this.sys.toDomain(),
        timezone = this.timezone,
        visibility = this.visibility,
        weather = this.weather.map {it.toDomain()} as MutableList<Weather>,
        wind = this.wind.toDomain()

    )
}