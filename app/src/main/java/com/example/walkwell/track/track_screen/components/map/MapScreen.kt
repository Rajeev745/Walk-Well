package com.example.walkwell.track.track_screen.components.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.walkwell.R
import com.example.walkwell.track.track_screen.components.TrackingViewModel
import com.example.walkwell.track.track_screen.components.service.TrackingService
import com.example.walkwell.utilities.ComponentTags.Companion.MAP_TAGS
import com.example.walkwell.utilities.UtilitiesFunctions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(trackingViewmodel: TrackingViewModel) {

    val pathPoints by TrackingService.pathPoints.collectAsState(emptyList())

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 17f)
    }

    // State for the marker's position
    val markerState = remember { MarkerState(position = LatLng(0.0, 0.0)) }

    Log.d(MAP_TAGS, "MapScreen: ${pathPoints.size}")

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                myLocationButtonEnabled = true
            )
        )
    }

    val context = LocalContext.current
    val userLocation = trackingViewmodel.userLocation
    val allPermissionsGranted = trackingViewmodel.allPermissionsGranted

    LaunchedEffect(allPermissionsGranted) {
        if (allPermissionsGranted.value) {
            trackingViewmodel.fetchCurrentLocation()
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings
    ) {
        LaunchedEffect(pathPoints, userLocation.value) {
            val latestLocation = when {
                pathPoints.isNotEmpty() -> {
                    val lastPoints = pathPoints.last()
                    LatLng(lastPoints[0], lastPoints[1])
                }

                userLocation.value != null -> {
                    userLocation.value
                }

                else -> null
            }

            latestLocation?.let { location ->
                markerState.position = location
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(location, 17f), 1000)
            }

        }
        Marker(
            state = markerState,
            title = "Location Tracking",
            snippet = "Your current location",
            icon = BitmapDescriptorFactory.fromBitmap(
                UtilitiesFunctions.convertDrawableToBitmap(
                    context,
                    R.drawable.ic_custom_marker_image,
                    30
                )
            )
        )

        // Polyline for the path
        if (pathPoints.isNotEmpty()) {
            Polyline(
                points = pathPoints.map { LatLng(it[0], it[1]) },
                color = Color.Red,
                width = 8f
            )
        }
    }
}