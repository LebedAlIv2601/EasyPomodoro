package com.something.easypomodoro.feature.timer.domain.usecase

import com.something.easypomodoro.feature.timer.domain.repository.TimerRepository

class StopTimerUseCase(
    private val timerRepository: TimerRepository
) {
    operator fun invoke() {
        timerRepository.stop()
    }
}
