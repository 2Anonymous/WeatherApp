package com.sukdeb.weatherApp.ui.dashBoard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukdeb.weatherApp.domain.responseModel.Resource
import com.sukdeb.weatherApp.domain.useCase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: WeatherUseCase): ViewModel() {

    private val _weatherState = MutableLiveData<HomeState>()
    val weatherState: LiveData<HomeState> = _weatherState

    fun onWeatherCall(lat: String, lon: String, unit: String, appId: String) {
        _weatherState.postValue(HomeState.Loading)

        viewModelScope.launch(Dispatchers.Main) {
            useCase.getWeatherReport(lat, lon, unit, appId).let {resource ->
                when(resource) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        _weatherState.postValue(HomeState.Error(resource.message!!))
                    }
                    is Resource.Success -> {
                        resource.data?.let {
                            _weatherState.postValue(HomeState.WeatherSuccess(resource.data, resource.message!!))
                        }
                    }
                }
            }
        }
    }
}