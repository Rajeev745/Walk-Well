package com.example.walkwell.track

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.walkwell.track.track_screen.components.TrackScreen
import com.example.walkwell.track.track_screen.components.TrackingViewModel
import com.example.walkwell.track.ui.theme.WalkWellTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalkWellTheme {
                val modifier = Modifier
                    .fillMaxSize()

                val trackingViewModel: TrackingViewModel by viewModels()
                Box(modifier = Modifier.fillMaxSize()) {

                    // Use your TrackScreen composable
                    TrackScreen(modifier, trackingViewModel)
                }
            }
        }
    }
}
