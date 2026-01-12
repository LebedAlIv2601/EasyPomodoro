package com.something.easypomodoro.feature.timer.di

import com.something.easypomodoro.feature.timer.data.TimerRepositoryImpl
import com.something.easypomodoro.feature.timer.domain.repository.TimerRepository
import com.something.easypomodoro.feature.timer.domain.usecase.ObserveTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.ResetTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.StartTimerUseCase
import com.something.easypomodoro.feature.timer.domain.usecase.StopTimerUseCase
import com.something.easypomodoro.feature.timer.presentation.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val timerModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    single<TimerRepository> { TimerRepositoryImpl(get()) }

    factory { ObserveTimerUseCase(get()) }
    factory { StartTimerUseCase(get()) }
    factory { StopTimerUseCase(get()) }
    factory { ResetTimerUseCase(get()) }

    viewModel { TimerViewModel(get(), get(), get(), get()) }
}
