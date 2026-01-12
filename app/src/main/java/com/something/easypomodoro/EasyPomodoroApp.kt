package com.something.easypomodoro

import android.app.Application
import com.something.easypomodoro.feature.timer.di.timerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class EasyPomodoroApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@EasyPomodoroApp)
            modules(timerModule)
        }
    }
}
