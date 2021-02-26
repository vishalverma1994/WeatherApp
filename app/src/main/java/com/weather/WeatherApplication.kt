package com.weather

import android.app.Application
import com.weather.di.appModule
import com.weather.di.repoModule
import com.weather.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            androidContext(this@WeatherApplication)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}