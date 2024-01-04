package com.sukdeb.weatherApp.domain.dataModels.weather

data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)