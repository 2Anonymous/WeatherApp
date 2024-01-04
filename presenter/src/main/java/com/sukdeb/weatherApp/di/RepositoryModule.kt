package com.sukdeb.weatherApp.di

import com.sukdeb.weatherApp.data.dataSource.repository.WeatherRepositoryImpl
import com.sukdeb.weatherApp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindRepositoryImpl(repositoryImpl: WeatherRepositoryImpl): WeatherRepository
}