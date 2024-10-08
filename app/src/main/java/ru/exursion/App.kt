package ru.exursion

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import ru.bibaboba.kit.Logger
import ru.bibaboba.kit.ui.utils.ThemeUtil
import ru.exursion.data.network.AuthHeaderInterceptor
import ru.exursion.di.AppComponent
import ru.exursion.di.AppModule
import ru.exursion.di.DaggerAppComponent
import ru.exursion.domain.settings.AppSettings
import ru.exursion.domain.settings.UserSettings
import javax.inject.Inject

class App : Application() {

    @Inject lateinit var userSettings: UserSettings
    @Inject lateinit var appSettings: AppSettings

    var appComponent: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()

        Logger.init(BuildConfig.DEBUG)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
            .also { it.inject(this) }

        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)

        userSettings.token?.let { AuthHeaderInterceptor.setSessionToken(it) }
        ThemeUtil.setDarkThemeState(appSettings.darkThemeState)
    }
}