package ru.exursion

import android.app.Application
import ru.exursion.di.AppComponent
import ru.exursion.di.AppModule
import ru.exursion.di.DaggerAppComponent

class App : Application() {

    var appComponent: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
    }
}