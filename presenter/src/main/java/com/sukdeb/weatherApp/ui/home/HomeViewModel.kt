package com.sukdeb.weatherApp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukdeb.weatherApp.data.dto.user.UserDto
import com.sukdeb.weatherApp.domain.repository.WeatherRepository
import com.sukdeb.weatherApp.domain.responseModel.Resource
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    private val _weatherStatus = MutableLiveData<Resource<UserDto>>()
    val weatherStatus: LiveData<Resource<UserDto>> = _weatherStatus

    suspend fun getWeatherReport(lat: String, lon: String, unit: String, appId: String) {
        _weatherStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val report = repository.getWeatherReport(lat, lon, unit, appId)
            _weatherStatus.postValue(report)
        }
    }
}