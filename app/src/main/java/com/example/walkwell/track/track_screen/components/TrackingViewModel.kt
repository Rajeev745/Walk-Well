package com.example.walkwell.track.track_screen.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.walkwell.utilities.LocationManager
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for handling the tracking data and permissions
 */
@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val locationManager: LocationManager,
) : ViewModel() {

    // for handling the permissions
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    // For getting the user location
    private val _userLocation = mutableStateOf<LatLng?>(null)
    val userLocation: State<LatLng?> = _userLocation

    // For checking whether all the permissions are granted
    private val _allPermissionsGranted = mutableStateOf(false)
    val allPermissionsGranted: State<Boolean> = _allPermissionsGranted

    /**
     * Method for updating the status whether all the permissions are granted or not.
     *
     * @param granted to check whether all the permissions are granted or not
     */
    fun updatePermissionsGrantedState(granted: Boolean) {
        _allPermissionsGranted.value = granted
    }

    /**
     * Dismisses the currently visible permission dialog by removing it from the queue.
     * Requires Android API level VANILLA_ICE_CREAM or higher.
     */
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    /**
     * Fetches the user's current location via the LocationManager.
     * Updates the userLocation state on success or logs an error message on failure.
     */
    fun fetchCurrentLocation() {
        locationManager.fetchCurrentLocation(
            onSuccess = { location -> _userLocation.value = location },
            onFailure = { error -> Log.d(TAG, error) }
        )
    }

    /**
     * Handles the result of a permission request.
     * Adds the permission to the dialog queue if it was denied and not already queued.
     *
     * @param permission The permission that was requested.
     * @param isGranted Whether the permission was granted (true) or denied (false).
     */
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    companion object {
        const val TAG = "TrackingViewModel"
    }
}