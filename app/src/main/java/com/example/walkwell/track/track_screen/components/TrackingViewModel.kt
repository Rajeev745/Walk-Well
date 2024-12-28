package com.example.walkwell.track.track_screen.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.walkwell.utilities.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
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

    private val _pathPoints = MutableLiveData<List<List<Double>>>(emptyList())
    val pathPoints: LiveData<List<List<Double>>> get() = _pathPoints

    // For checking whether all the permissions are granted
    private val _allPermissionsGranted = mutableStateOf(false)
    val allPermissionsGranted: State<Boolean> = _allPermissionsGranted

    private val _totalDistance = MutableLiveData<Double>()
    val totalDistance: LiveData<Double> = _totalDistance

    private var previousLocation = LatLng(0.0, 0.0)
    private var isFirstTimeDistance = true

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

    fun updatePathPoints(newPoint: List<Double>) {
        val latlng = LatLng(newPoint[0], newPoint[1])
        val updatedPathPoints = _pathPoints.value.orEmpty().toMutableList()
        updatedPathPoints.add(newPoint)

        if (isFirstTimeDistance) {
            val distance = SphericalUtil.computeDistanceBetween(latlng, latlng)
            _totalDistance.value = (_totalDistance.value ?: 0.0) + distance
            isFirstTimeDistance = false
        } else {
            previousLocation.let {
                val distance = SphericalUtil.computeDistanceBetween(it, latlng)
                _totalDistance.value = (_totalDistance.value ?: 0.0) + distance
            }
        }

        // Update previous location
        previousLocation = latlng

        // Update the path points
        _pathPoints.value = updatedPathPoints
    }

    companion object {
        const val TAG = "TrackingViewModel"
    }
}