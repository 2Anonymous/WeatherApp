package com.sukdeb.weatherApp.ui.splash

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.sukdeb.weatherApp.R
import com.sukdeb.weatherApp.ui.dashBoard.MainActivity
import com.sukdeb.weatherApp.ui.dashBoard.MainViewModel
import com.sukdeb.weatherApp.utils.ext.gotToActivityWithHashMap
import com.sukdeb.weatherApp.utils.ext.locationClient
import com.sukdeb.weatherApp.utils.ext.locationManager
import com.sukdeb.weatherApp.utils.ext.showOkayAlertFunction
import com.sukdeb.weatherApp.utils.location.GPSStatusObserver
import com.sukdeb.weatherApp.utils.location.GpsStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    //private val mainViewModel by viewModels<MainViewModel>()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private val map : HashMap<String, Double> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    lastLocation = p0.lastLocation
                    if (latitude == 0.0 && longitude == 0.0) {
                        latitude = (lastLocation)!!.latitude
                        longitude = (lastLocation)!!.longitude
                        map["lat"] = latitude
                        map["lon"] = longitude
                        /*mainViewModel.latitude.value = latitude
                        mainViewModel.longitude.value = longitude*/
                    }
                }
            }
        }

        initObserver()
        gotoMain(map)
    }

    private fun initObserver() {
        GPSStatusObserver(this).observe(this) {
            when (it) {
                is GpsStatus.Enabled -> { getLastLocation() }
                is GpsStatus.Disabled -> { enableGPS() }
            }
        }

    }

    private fun enableGPS() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val locationRequest: LocationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000
            locationRequest.fastestInterval = 5 * 1000
            val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            locationSettingsRequest.setAlwaysShow(true)
            val results: Task<LocationSettingsResponse> = locationClient.checkLocationSettings(locationSettingsRequest.build())
            results.addOnSuccessListener {
            }.addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        startForGPSResult.launch(IntentSenderRequest.Builder(it.resolution).build())
                    } catch (sendEx: IntentSender.SendIntentException) {
                        sendEx.printStackTrace()
                    }
                }
            }
        }
    }

    private var startForGPSResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            showOkayAlertFunction(
                "GPS Required", "Kindly allow/enable GPS otherwise app won't work.",
                positiveBtnClick = { enableGPS() })
        }
    }
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) { return }
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
            task.addOnSuccessListener { _ ->
                fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        exception.startResolutionForResult(this@SplashActivity, 101)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        sendEx.printStackTrace()
                    }
                }
            }
        }
    }

    private fun gotoMain(myMap: HashMap<String, Double>) {
        lifecycleScope.launch {
            delay(1500)
            gotToActivityWithHashMap(MainActivity::class.java, myMap)
        }
    }
}