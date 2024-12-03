package com.example.walkwell.home.home_screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.walkwell.R
import com.example.walkwell.track.TrackActivity
import com.example.walkwell.ui_components.buttons.CircularButton

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CircularButton(
                    text = "Start Run",
                    onClick = { openTrackActivity(context) },
                    size = 200.dp,
                    backGroundColor = colorResource(id = R.color.primary_color)
                )
            }
        }
    }
}

/**
 * Method for starting the new activity for tracking the run
 *
 * @param context - context of the activity
 */
private fun openTrackActivity(context: Context) {
    val intent = Intent(context, TrackActivity::class.java)
    context.startActivity(intent)
}