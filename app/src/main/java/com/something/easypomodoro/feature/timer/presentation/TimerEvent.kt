package com.something.easypomodoro.feature.timer.presentation

sealed interface TimerEvent {
    data object OnStartClick : TimerEvent
    data object OnStopClick : TimerEvent
    data object OnResetClick : TimerEvent
}
