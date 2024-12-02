package com.example.walkwell.home.home_screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Button(onClick = {
        navController.navigate("second_screen") // Navigate to second screen
    }) {
        Text("Go to Second Screen")
    }
}

@Composable
fun SecondScreen() {
    // Your second screen UI
    Text("Welcome to the Second Screen!")
}