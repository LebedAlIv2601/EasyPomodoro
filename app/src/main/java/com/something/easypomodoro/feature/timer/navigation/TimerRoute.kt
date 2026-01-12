package com.something.easypomodoro.feature.timer.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.something.easypomodoro.feature.timer.presentation.TimerScreen
import kotlinx.serialization.Serializable

@Serializable
data object TimerRoute

fun NavGraphBuilder.timerScreen() {
    composable<TimerRoute> {
        TimerScreen()
    }
}
