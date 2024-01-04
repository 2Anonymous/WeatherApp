package com.sukdeb.weatherApp.di

import com.sukdeb.weatherApp.data.dataSource.network.DataSource
import com.sukdeb.weatherApp.data.dataSource.network.DataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindNetworkDataSourceImpl(networkDataSource:DataSourceImpl): DataSource
}