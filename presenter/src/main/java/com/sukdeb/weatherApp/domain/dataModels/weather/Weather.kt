package com.sukdeb.weatherApp.domain.dataModels.weather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)