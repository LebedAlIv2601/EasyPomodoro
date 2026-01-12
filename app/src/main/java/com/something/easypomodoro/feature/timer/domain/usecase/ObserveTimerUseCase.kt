package com.something.easypomodoro.feature.timer.domain.usecase

import com.something.easypomodoro.feature.timer.domain.model.TimerState
import com.something.easypomodoro.feature.timer.domain.repository.TimerRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveTimerUseCase(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): StateFlow<TimerState> {
        return timerRepository.timerState
    }
}
