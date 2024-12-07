package com.example.walkwell.track.track_screen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.walkwell.track.track_screen.components.map.MapScreen
import com.example.walkwell.track.track_screen.components.tracking_permissions.Permissions

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TrackScreen(modifier: Modifier = Modifier, trackingViewmodel: TrackingViewModel) {

    Box(
        modifier = modifier
    ) {
        Permissions(trackingViewmodel)
        MapScreen(trackingViewmodel)
    }
}