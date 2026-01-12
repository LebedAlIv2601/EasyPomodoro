package com.something.easypomodoro.feature.timer.domain.model

sealed class PomodoroPhase(val durationSeconds: Int) {
    data class Work(val sessionNumber: Int) : PomodoroPhase(WORK_DURATION_SECONDS)
    data class ShortBreak(val afterSession: Int) : PomodoroPhase(SHORT_BREAK_DURATION_SECONDS)
    data object LongBreak : PomodoroPhase(LONG_BREAK_DURATION_SECONDS)

    companion object {
        const val WORK_DURATION_SECONDS = 25 * 60
        const val SHORT_BREAK_DURATION_SECONDS = 5 * 60
        const val LONG_BREAK_DURATION_SECONDS = 15 * 60
        const val TOTAL_WORK_SESSIONS = 4
    }
}

fun PomodoroPhase.nextPhase(): PomodoroPhase {
    return when (this) {
        is PomodoroPhase.Work -> {
            if (sessionNumber < PomodoroPhase.TOTAL_WORK_SESSIONS) {
                PomodoroPhase.ShortBreak(afterSession = sessionNumber)
            } else {
                PomodoroPhase.LongBreak
            }
        }
        is PomodoroPhase.ShortBreak -> {
            PomodoroPhase.Work(sessionNumber = afterSession + 1)
        }
        is PomodoroPhase.LongBreak -> {
            PomodoroPhase.Work(sessionNumber = 1)
        }
    }
}
