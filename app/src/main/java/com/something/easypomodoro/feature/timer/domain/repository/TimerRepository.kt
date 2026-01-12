package com.something.easypomodoro.feature.timer.domain.repository

import com.something.easypomodoro.feature.timer.domain.model.TimerState
import kotlinx.coroutines.flow.StateFlow

interface TimerRepository {
    val timerState: StateFlow<TimerState>

    fun start()
    fun stop()
    fun reset()
}
