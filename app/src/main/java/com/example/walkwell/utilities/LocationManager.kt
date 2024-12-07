package com.example.walkwell.utilities

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

/**
 * A helper class to manage fetching the user's current location using FusedLocationProviderClient.
 * Handles permission checks and provides callbacks for success or failure.
 *
 * @property context The application context used for permission checks.
 * @property fusedLocationProviderClient The client used to retrieve the user's last known location.
 */
class LocationManager @Inject constructor(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) {

    /**
     * Fetches the user's current location.
     * Calls the success callback with the latitude and longitude if the location is available.
     * Calls the failure callback with an error message if permissions are missing or an exception occurs.
     *
     * @param onSuccess A callback invoked with the user's location as a LatLng object.
     * @param onFailure A callback invoked with an error message in case of failure.
     */
    fun fetchCurrentLocation(onSuccess: (LatLng) -> Unit, onFailure: (String) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        Log.d(TAG, "fetchCurrentLocation: $location")
                        val latLng = LatLng(location.latitude, location.longitude)
                        onSuccess(latLng)
                    } ?: onFailure("Location is null")
                }
            } catch (e: SecurityException) {
                Log.i(TAG, "fetchCurrentLocation: ${e.localizedMessage}")
                onFailure(e.localizedMessage ?: "SecurityException occurred")
            }
        } else {
            Log.d(TAG, "Permission Not Granted")
            onFailure("Permission not granted")
        }
    }

    companion object {
        const val TAG = "LocationManager"
    }
}