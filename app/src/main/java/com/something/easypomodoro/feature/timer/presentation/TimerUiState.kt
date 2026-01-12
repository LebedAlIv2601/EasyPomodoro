package com.something.easypomodoro.feature.timer.presentation

data class TimerUiState(
    val timeFormatted: String,
    val phaseLabel: String,
    val isRunning: Boolean,
    val progress: Float,
    val phaseType: PhaseType
) {
    companion object {
        val Initial = TimerUiState(
            timeFormatted = "25:00",
            phaseLabel = "Work 1/4",
            isRunning = false,
            progress = 1f,
            phaseType = PhaseType.WORK
        )
    }
}

enum class PhaseType {
    WORK,
    SHORT_BREAK,
    LONG_BREAK
}
