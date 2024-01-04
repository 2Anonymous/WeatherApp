package com.sukdeb.weatherApp.di

import android.content.Context
import android.content.SharedPreferences
import com.sukdeb.weatherApp.data.preference.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    fun getPreference(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceHelper.defaultPrefs(context)
    }
}