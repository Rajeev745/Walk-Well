package com.example.walkwell.home.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.walkwell.home.bottom_nav.BottomNavCurve
import com.example.walkwell.home.bottom_nav.BottomNavScreen
import com.example.walkwell.home.history.HistoryScreen
import com.example.walkwell.home.home_screen.HomeScreen
import com.example.walkwell.home.profile.ProfileScreen

@Composable
fun MainScreen(modifier: Modifier) {

    val navController = rememberNavController()

    val bottomNavList = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.History,
        BottomNavScreen.Profile
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    var animatedIndex = remember { Animatable(selectedIndex.toFloat()) }

    LaunchedEffect(selectedIndex) {
        animatedIndex.animateTo(
            targetValue = selectedIndex.toFloat(),
            animationSpec = tween(durationMillis = 300)
        )
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .height(115.dp)
                    .clip(BottomNavCurve(selectedIndex, bottomNavList.size))
            ) {
                bottomNavList.forEachIndexed { index, bottomNavScreen ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        icon = {
                            if (selectedIndex == index) {
                                Icon(
                                    painter = painterResource(id = bottomNavScreen.icon),
                                    contentDescription = bottomNavScreen.title
                                )
                            }
                        },
                        label = { Text(text = bottomNavScreen.title) },
                        onClick = {
                            selectedIndex = index
                            navController.navigate(
                                bottomNavScreen.route
                            ) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Home.route) { HomeScreen(navController) }
            composable(BottomNavScreen.History.route) { HistoryScreen() }
            composable(BottomNavScreen.Profile.route) { ProfileScreen() }
        }
    }

}