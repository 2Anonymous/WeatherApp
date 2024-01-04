package com.sukdeb.weatherApp.data.dataSource.network

import android.content.Context
import android.content.SharedPreferences
import com.sukdeb.weatherApp.data.preference.PreferenceHelper.clearPreference
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator@Inject constructor(@ApplicationContext private val context: Context
, private val preferences: SharedPreferences): Interceptor, Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (!response.isSuccessful && response.code == 401) {
            val myMap: HashMap<String, String> = HashMap()
            myMap["tokenExpired"] = "true"
            preferences.clearPreference()
            //context.gotToActivityWithHashMap(OnBoardingActivity::class.java, myMap)
            return null
        }
        return response.request.newBuilder().build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().apply {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }.build()
        return chain.proceed(request)
    }
}