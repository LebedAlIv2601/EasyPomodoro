package com.something.easypomodoro.feature.timer.data

import com.something.easypomodoro.feature.timer.domain.model.PomodoroPhase
import com.something.easypomodoro.feature.timer.domain.model.TimerState
import com.something.easypomodoro.feature.timer.domain.model.nextPhase
import com.something.easypomodoro.feature.timer.domain.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerRepositoryImpl(
    private val scope: CoroutineScope
) : TimerRepository {

    private val _timerState = MutableStateFlow(TimerState.Initial)
    override val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    override fun start() {
        if (_timerState.value.isRunning) return

        _timerState.update { it.copy(isRunning = true) }

        timerJob = scope.launch {
            while (_timerState.value.isRunning && _timerState.value.remainingSeconds > 0) {
                delay(1000L)
                if (_timerState.value.isRunning) {
                    tick()
                }
            }
        }
    }

    override fun stop() {
        timerJob?.cancel()
        timerJob = null
        _timerState.update { it.copy(isRunning = false) }
    }

    override fun reset() {
        stop()
        _timerState.value = TimerState.Initial
    }

    private fun tick() {
        _timerState.update { currentState ->
            val newRemainingSeconds = currentState.remainingSeconds - 1
            if (newRemainingSeconds <= 0) {
                val nextPhase = currentState.phase.nextPhase()
                currentState.copy(
                    phase = nextPhase,
                    remainingSeconds = nextPhase.durationSeconds
                )
            } else {
                currentState.copy(remainingSeconds = newRemainingSeconds)
            }
        }
    }
}
