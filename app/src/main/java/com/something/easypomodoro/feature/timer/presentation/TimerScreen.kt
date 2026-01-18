package com.something.easypomodoro.feature.timer.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.something.easypomodoro.ui.components.CircularTimer
import com.something.easypomodoro.ui.components.PomodoroButton
import com.something.easypomodoro.ui.components.PomodoroOutlinedButton
import com.something.easypomodoro.ui.theme.BreakPrimary
import com.something.easypomodoro.ui.theme.BreakPrimaryDark
import com.something.easypomodoro.ui.theme.BreakSecondary
import com.something.easypomodoro.ui.theme.BreakSecondaryDark
import com.something.easypomodoro.ui.theme.LongBreakPrimary
import com.something.easypomodoro.ui.theme.LongBreakPrimaryDark
import com.something.easypomodoro.ui.theme.LongBreakSecondary
import com.something.easypomodoro.ui.theme.LongBreakSecondaryDark
import com.something.easypomodoro.ui.theme.WorkPrimary
import com.something.easypomodoro.ui.theme.WorkPrimaryDark
import com.something.easypomodoro.ui.theme.WorkSecondary
import com.something.easypomodoro.ui.theme.WorkSecondaryDark
import org.koin.androidx.compose.koinViewModel

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TimerScreenContent(
        state = state,
        onEvent = viewModel::onUiEvent
    )
}

@Composable
private fun TimerScreenContent(
    state: TimerUiState,
    onEvent: (TimerEvent) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    val progressColor by animateColorAsState(
        targetValue = state.phaseType.toProgressColor(isDarkTheme),
        animationSpec = tween(durationMillis = 500),
        label = "progressColor"
    )

    val trackColor by animateColorAsState(
        targetValue = state.phaseType.toTrackColor(isDarkTheme),
        animationSpec = tween(durationMillis = 500),
        label = "trackColor"
    )

    val buttonsColor = remember(state.buttonsColor) {
        if(state.buttonsColor == ButtonsColor.STANDART) progressColor else Color.Blue
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            CircularTimer(
                timeFormatted = state.timeFormatted,
                phaseLabel = state.phaseLabel,
                progress = state.progress,
                progressColor = progressColor,
                trackColor = trackColor
            )

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.isRunning) {
                    PomodoroButton(
                        text = "Stop",
                        onClick = { onEvent(TimerEvent.OnStopClick) },
                        containerColor = buttonsColor
                    )
                } else {
                    PomodoroButton(
                        text = "Start",
                        onClick = { onEvent(TimerEvent.OnStartClick) },
                        containerColor = buttonsColor
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    PomodoroOutlinedButton(
                        text = "Reset",
                        onClick = { onEvent(TimerEvent.OnResetClick) },
                        borderColor = buttonsColor,
                        contentColor = buttonsColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PomodoroOutlinedButton(
                text = "ChangeColor",
                onClick = { onEvent(TimerEvent.OnChangeColorClick) },
                borderColor = buttonsColor,
                contentColor = buttonsColor
            )
        }
    }
}

private fun PhaseType.toProgressColor(isDarkTheme: Boolean): Color {
    return when (this) {
        PhaseType.WORK -> if (isDarkTheme) WorkPrimaryDark else WorkPrimary
        PhaseType.SHORT_BREAK -> if (isDarkTheme) BreakPrimaryDark else BreakPrimary
        PhaseType.LONG_BREAK -> if (isDarkTheme) LongBreakPrimaryDark else LongBreakPrimary
    }
}

private fun PhaseType.toTrackColor(isDarkTheme: Boolean): Color {
    return when (this) {
        PhaseType.WORK -> if (isDarkTheme) WorkSecondaryDark else WorkSecondary
        PhaseType.SHORT_BREAK -> if (isDarkTheme) BreakSecondaryDark else BreakSecondary
        PhaseType.LONG_BREAK -> if (isDarkTheme) LongBreakSecondaryDark else LongBreakSecondary
    }
}
