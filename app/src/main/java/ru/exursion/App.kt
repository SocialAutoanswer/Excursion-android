package ru.exursion

import android.app.Application

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