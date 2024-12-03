package com.example.walkwell.track.track_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TrackScreen(modifier: Modifier = Modifier) {

    Box(modifier = modifier) {
        Text(text = "Welcome to tracking activity")
    }
}