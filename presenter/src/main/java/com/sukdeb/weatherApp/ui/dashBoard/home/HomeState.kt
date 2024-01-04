package com.sukdeb.weatherApp.ui.dashBoard.home

import com.sukdeb.weatherApp.domain.dataModels.weather.WeatherModel

sealed class HomeState {
    object Loading : HomeState()
    data class Error(val errorMessage:String) : HomeState()
    data class WeatherSuccess(val weatherData: WeatherModel,val successMessage: String) : HomeState()
}