package com.sukdeb.weatherApp.di

import com.sukdeb.weatherApp.domain.repository.WeatherRepository
import com.sukdeb.weatherApp.domain.useCase.WeatherUseCase
import com.sukdeb.weatherApp.domain.useCase.WeatherUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun bindWeatherUseCaseModule(weatherRepository: WeatherRepository): WeatherUseCase = WeatherUseCaseImpl(weatherRepository)
}