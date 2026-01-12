package com.something.easypomodoro.feature.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.something.easypomodoro.feature.timer.domain.model.PomodoroPhase
import com.something.easypomodoro.feature.timer.domain.model.TimerState
import com.something.easypomodoro.feature.timer.domain.usecase.ObserveTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.ResetTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.StartTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.StopTimerUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TimerViewModel(
    observeTimerUseCase: ObserveTimerUseCase,
    private val startTimerUseCase: StartTimerUseCase,
    private val stopTimerUseCase: StopTimerUseCase,
    private val resetTimerUseCase: ResetTimerUseCase
) : ViewModel() {

    val state: StateFlow<TimerUiState> = observeTimerUseCase()
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TimerUiState.Initial
        )

    fun onUiEvent(event: TimerEvent) {
        when (event) {
            TimerEvent.OnStartClick -> startTimerUseCase()
            TimerEvent.OnStopClick -> stopTimerUseCase()
            TimerEvent.OnResetClick -> resetTimerUseCase()
        }
    }

    private fun TimerState.toUiState(): TimerUiState {
        return TimerUiState(
            timeFormatted = formatTime(remainingSeconds),
            phaseLabel = phase.toLabel(),
            isRunning = isRunning,
            progress = progress,
            phaseType = phase.toPhaseType()
        )
    }

    private fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "$minutes:${seconds.toString().padStart(2, '0')}"
    }

    private fun PomodoroPhase.toLabel(): String {
        return when (this) {
            is PomodoroPhase.Work -> "Work $sessionNumber/${PomodoroPhase.TOTAL_WORK_SESSIONS}"
            is PomodoroPhase.ShortBreak -> "Short Break"
            is PomodoroPhase.LongBreak -> "Long Break"
        }
    }

    private fun PomodoroPhase.toPhaseType(): PhaseType {
        return when (this) {
            is PomodoroPhase.Work -> PhaseType.WORK
            is PomodoroPhase.ShortBreak -> PhaseType.SHORT_BREAK
            is PomodoroPhase.LongBreak -> PhaseType.LONG_BREAK
        }
    }
}
