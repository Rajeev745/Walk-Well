package com.example.walkwell.track.track_screen.components

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.walkwell.R
import com.example.walkwell.track.track_screen.components.map.MapScreen
import com.example.walkwell.track.track_screen.components.service.TrackingService
import com.example.walkwell.track.track_screen.components.tracking_permissions.Permissions
import com.example.walkwell.utilities.ComponentTags.Companion.PAUSE_SERVICE
import com.example.walkwell.utilities.ComponentTags.Companion.START_SERVICE
import com.example.walkwell.utilities.ComponentTags.Companion.STOP_SERVICE
import com.example.walkwell.utilities.UtilitiesFunctions

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TrackScreen(modifier: Modifier = Modifier, trackingViewmodel: TrackingViewModel) {

    val isTracking = TrackingService.isTracking.observeAsState()
    val timeRunInMillis = TrackingService.timeRunInMillis.observeAsState()
    val distanceTravelled = trackingViewmodel.totalDistance
    val context = LocalContext.current

    // Set up the scroll state
    val scrollState = rememberScrollState()

    // Define the scroll behavior for TopAppBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Get the screen height and set 70% as the initial height for the map
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val minHeight = screenHeight * 0.2f // Minimum height is 20% of the screen
    val maxHeight = screenHeight * 0.7f // Maximum height is 70% of the screen

    // Scroll progress is used to control the height of the map dynamically
    val scrollProgress = scrollState.value / screenHeight.value

    // Calculate the dynamic height based on scroll position but clamp it between minHeight and maxHeight
    val targetMapHeight = (maxHeight - minHeight) * (1 - scrollProgress) + minHeight

    // Smoothly animate the map height change, clamping it to ensure it doesn't go below minHeight
    val mapHeight by animateDpAsState(
        targetValue = targetMapHeight.coerceIn(minHeight, maxHeight),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing) // Adjust duration for smoothness
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // The Map view which collapses on scroll
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(mapHeight) // Dynamically changing height based on scroll
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            MapScreen(trackingViewmodel) // Your map screen component
        }

        // Permissions Section
        Permissions(trackingViewmodel)

        // Content below the map
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState) // Enable vertical scroll for the rest of the content
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(2000.dp)) // Spacer between sections

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularButton(
                    onClick = {
                        sendCommandToService(
                            context = context,
                            action = if (isTracking.value == true) PAUSE_SERVICE else START_SERVICE
                        )
                    },
                    icon = if (isTracking.value == true) R.drawable.ic_pause_tracking else R.drawable.ic_start_tracking,
                    text = "start"
                )

                Card(
                    Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            text = UtilitiesFunctions.formatElapsedTime(
                                timeRunInMillis.value ?: 0L
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(50.dp)
                                .background(color = Color.Blue)
                        )

                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            text = formatDistance(distanceTravelled.value ?: 0.0),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                CircularButton(
                    onClick = { sendCommandToService(context, STOP_SERVICE) },
                    icon = R.drawable.ic_stop_tracking,
                    text = "start"
                )
            }
        }
    }
}
/**
 * Starts the TrackingService with a specified action.
 *
 * This method creates an intent to start the TrackingService and sets its action.
 * The service is then started with the specified context and action.
 *
 * @param context The context used to start the service.
 * @param action The action to be set on the intent before starting the service.
 */
private fun sendCommandToService(context: Context, action: String) {
    Intent(context, TrackingService::class.java).also {
        it.action = action
        context.startService(it)
    }
}

/**
 * Formats a given distance in meters to a human-readable string.
 *
 * If the distance is less than 1000 meters, it returns the distance in meters, suffixed with "m".
 * If the distance is 1000 meters or more, it converts the distance to kilometers,
 * and formats it to two decimal places, suffixed with "km".
 *
 * @param distanceInMeters The distance in meters to be formatted.
 * @return A string representing the formatted distance in meters or kilometers.
 */
private fun formatDistance(distanceInMeters: Double): String {
    return if (distanceInMeters < 1000) {
        // Show distance in meters
        "${distanceInMeters.toInt()} m"
    } else {
        // Convert to kilometers and show up to 2 decimal places
        "%.2f km".format(distanceInMeters / 1000)
    }
}