package com.sukdeb.weatherApp.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.sukdeb.weatherApp.BuildConfig
import com.sukdeb.weatherApp.data.dataSource.network.ApiService
import com.sukdeb.weatherApp.data.dataSource.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTokenAuthenticator(@ApplicationContext context: Context, preferences: SharedPreferences): TokenAuthenticator {
        return TokenAuthenticator(context, preferences)
    }

    @Singleton
    @Provides
    fun provideOkHttpClientWithHeaders(tokenAuthenticator: TokenAuthenticator): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        // set your desired log level
        httpLoggingInterceptor.setLevel((HttpLoggingInterceptor.Level.BODY))
        return OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(tokenAuthenticator)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}