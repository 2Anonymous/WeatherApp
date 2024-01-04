package com.sukdeb.weatherApp.data.dataSource.network

import com.sukdeb.weatherApp.data.dto.user.UserDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("2.5/weather")
    fun getWeatherReport(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String?,
        @Query("appid") appId: String?
    ): UserDto
}