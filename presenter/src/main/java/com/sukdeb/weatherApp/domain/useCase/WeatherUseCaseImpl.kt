package com.sukdeb.weatherApp.domain.useCase

import com.sukdeb.weatherApp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherUseCaseImpl @Inject constructor(private val weatherRepository: WeatherRepository): WeatherUseCase {}