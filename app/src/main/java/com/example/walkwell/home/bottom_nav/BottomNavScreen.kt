package com.example.walkwell.home.bottom_nav

import com.example.walkwell.R

sealed class BottomNavScreen(val route: String, val title: String, val icon: Int) {

    data object Home : BottomNavScreen("home", "Home", R.drawable.ic_home)
    data object History : BottomNavScreen("history", "History", R.drawable.ic_history)
    data object Profile : BottomNavScreen("profile", "Profile", R.drawable.ic_profile)
}