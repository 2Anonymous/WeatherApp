package com.sukdeb.weatherApp.ui.dashBoard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    val latitude = MutableLiveData(0.0)
    val longitude = MutableLiveData(0.0)
}