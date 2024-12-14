package com.example.walkwell.track.track_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = Color.DarkGray,
    icon: Int? = null,
    text: String? = null,
) {

    Box(
        modifier = Modifier
            .size(56.dp)
            .clickable { onClick() }
            .background(backgroundColor, CircleShape)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text ?: "Circular Button",
                tint = Color.Unspecified
            )
        } else if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}