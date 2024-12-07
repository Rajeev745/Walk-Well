package com.example.walkwell.track.track_screen.components.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.walkwell.R
import com.example.walkwell.track.track_screen.components.TrackingViewModel
import com.example.walkwell.utilities.UtilitiesFunctions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(trackingViewmodel: TrackingViewModel) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 17f)
    }

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
        userLocation.value?.let {
            Marker(
                state = MarkerState(position = it),
                title = "User Current location",
                snippet = "This is your current location",
                icon = BitmapDescriptorFactory.fromBitmap(
                    UtilitiesFunctions.convertDrawableToBitmap(
                        context,
                        R.drawable.ic_custom_marker_image,
                        30                    )
                )
            )
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 17f)
        }
    }
}