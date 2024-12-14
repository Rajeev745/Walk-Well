package com.example.walkwell.track.track_screen.components

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TrackScreen(modifier: Modifier = Modifier, trackingViewmodel: TrackingViewModel) {

    val isTracking = TrackingService.isTracking.observeAsState()
    val timeRunInMillis = TrackingService.timeRunInMillis.observeAsState()
    val context = LocalContext.current

    Box(
        modifier = modifier
    ) {
        Permissions(trackingViewmodel)
        MapScreen(trackingViewmodel)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

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
                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 15.dp, vertical = 10.dp),
                        text = UtilitiesFunctions.formatElapsedTime(timeRunInMillis.value ?: 0L),
                        style = MaterialTheme.typography.bodyLarge
                    )
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