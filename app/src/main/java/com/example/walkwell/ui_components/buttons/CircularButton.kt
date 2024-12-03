package com.example.walkwell.ui_components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    size: Dp = 56.dp,
    backGroundColor: Color = MaterialTheme.colorScheme.primary,
    text: String,
) {

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backGroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White, // Set text color
            fontSize = 30.sp,  // Set font size
            fontWeight = FontWeight.Bold, // Set font weight (Bold)
            textAlign = TextAlign.Center, // Align text (Center)
            modifier = Modifier.fillMaxWidth()
        )
    }
}