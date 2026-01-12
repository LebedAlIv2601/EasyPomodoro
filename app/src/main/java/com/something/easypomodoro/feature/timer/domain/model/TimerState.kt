package com.something.easypomodoro.feature.timer.domain.model

data class TimerState(
    val phase: PomodoroPhase,
    val remainingSeconds: Int,
    val isRunning: Boolean
) {
    val progress: Float
        get() = remainingSeconds.toFloat() / phase.durationSeconds

    companion object {
        val Initial = TimerState(
            phase = PomodoroPhase.Work(sessionNumber = 1),
            remainingSeconds = PomodoroPhase.WORK_DURATION_SECONDS,
            isRunning = false
        )
    }
}
