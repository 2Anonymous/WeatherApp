package com.sukdeb.weatherApp.data.dto.weather

import com.google.gson.annotations.SerializedName
import com.sukdeb.weatherApp.domain.dataModels.weather.Main

data class MainDto(
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("grnd_level")
    val grandLevel: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("sea_level")
    val seaLevel: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)

fun MainDto.toDomain(): Main {
    return Main(
        feels_like = this.feelsLike,
        grnd_level = this.grandLevel,
        humidity = this.humidity,
        pressure = this.pressure,
        temp = this.temp,
        temp_max = this.tempMax,
        temp_min = this.tempMin
    )
}