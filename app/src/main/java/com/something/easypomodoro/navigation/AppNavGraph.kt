package com.something.easypomodoro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.something.easypomodoro.feature.timer.navigation.TimerRoute
import com.something.easypomodoro.feature.timer.navigation.timerScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = TimerRoute
    ) {
        timerScreen()
    }
}
