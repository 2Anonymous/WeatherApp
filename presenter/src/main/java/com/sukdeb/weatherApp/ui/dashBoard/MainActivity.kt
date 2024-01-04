package com.sukdeb.weatherApp.ui.dashBoard

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
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
import com.sukdeb.weatherApp.databinding.ActivityMainBinding
import com.sukdeb.weatherApp.utils.ext.locationClient
import com.sukdeb.weatherApp.utils.ext.locationManager
import com.sukdeb.weatherApp.utils.ext.showOkayAlertFunction
import com.sukdeb.weatherApp.utils.permissions.goToAppDetailsSettings
import com.sukdeb.weatherApp.utils.location.GPSStatusObserver
import com.sukdeb.weatherApp.utils.location.GpsStatus
import com.sukdeb.weatherApp.utils.permissions.requestMultiplePermissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this
        val lat = intent.extras?.getDouble("lat")
        val long = intent.extras?.getDouble("long")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    lastLocation = p0.lastLocation
                    if (latitude == 0.0 && longitude == 0.0) {
                        latitude = (lastLocation)!!.latitude
                        longitude = (lastLocation)!!.longitude
                        viewModel.latitude.value = lat
                        viewModel.longitude.value = long
                    }
                }
            }
        }

        initObserver()
    }

    private fun initObserver() {
        GPSStatusObserver(this).observe(this) {
            when (it) {
                is GpsStatus.Enabled -> { getLastLocation() }
                is GpsStatus.Disabled -> { enableGPS() }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requestAllUserPermissions()
    }

    /**
     * request all Permissions form User
     * Storage Permission
     * Camera Permission
     * Internet Permission
     * if all permission will grant then user can start Profile creation process other wise intent to Phone Setting
     **/
    private fun requestAllUserPermissions() {
        requestMultiplePermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            allGranted = {/*Do your task here*/},
            denied = {
                // Permission application failed and it is unchecked, do not ask again, you can continue to apply next time
                showOkayAlertFunction(
                    getString(R.string.permission_denied_title),
                    getString(R.string.permission_denied_alert),
                    positiveBtnClick = { goToAppDetailsSettings() })
            },
            explained = {
                // Permission application failed and it has been checked Do not ask again, you need to explain the reason to the user and guide the user to open the permission
                showOkayAlertFunction(
                    getString(R.string.permission_denied_title),
                    getString(R.string.permission_denied_alert),
                    positiveBtnClick = { goToAppDetailsSettings()})
            })
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
                        exception.startResolutionForResult(this@MainActivity, 101)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        sendEx.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}