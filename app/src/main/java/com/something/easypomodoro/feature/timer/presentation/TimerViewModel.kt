package com.something.easypomodoro.feature.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.something.easypomodoro.feature.timer.domain.model.PomodoroPhase
import com.something.easypomodoro.feature.timer.domain.model.TimerState
import com.something.easypomodoro.feature.timer.domain.usecase.ObserveTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.ResetTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.StartTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.StopTimerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class TimerViewModel(
    observeTimerUseCase: ObserveTimerUseCase,
    private val startTimerUseCase: StartTimerUseCase,
    private val stopTimerUseCase: StopTimerUseCase,
    private val resetTimerUseCase: ResetTimerUseCase
) : ViewModel() {

    private val buttonsColor = MutableStateFlow(ButtonsColor.STANDART)

    val state: StateFlow<TimerUiState> = combine(
        observeTimerUseCase(),
        buttonsColor,
        ::createUiState
    )
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
            TimerEvent.OnChangeColorClick -> changeButtonsColor()
        }
    }

    private fun changeButtonsColor() {
        buttonsColor.update {
            if(it == ButtonsColor.STANDART) ButtonsColor.NOT_STANDART else ButtonsColor.STANDART
        }
    }

    private fun createUiState(timerState: TimerState, buttonsColor: ButtonsColor): TimerUiState {
        return TimerUiState(
            timeFormatted = formatTime(timerState.remainingSeconds),
            phaseLabel = timerState.phase.toLabel(),
            isRunning = timerState.isRunning,
            progress = timerState.progress,
            phaseType = timerState.phase.toPhaseType(),
            buttonsColor = buttonsColor
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
