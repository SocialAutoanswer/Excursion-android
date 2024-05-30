package ru.exursion.di

import dagger.Component
import ru.exursion.data.network.NetworkModule
import ru.exursion.ui.routes.fragments.ChooseCityFragment
import ru.exursion.ui.routes.fragments.TagsFragment
import ru.exursion.domain.settings.SettingsModule
import ru.exursion.ui.SplashActivity
import ru.exursion.ui.auth.fragments.EnterCodeFragment
import ru.exursion.ui.auth.fragments.EnterEmailFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    AppModule.Bind::class,
    SettingsModule::class,
    NetworkModule::class,
])
interface AppComponent {

    fun inject(activity: SplashActivity)

    fun inject(fragment: ChooseCityFragment)
    fun inject(fragment: TagsFragment)
    fun inject(fragment: EnterEmailFragment)
    fun inject(fragment: EnterCodeFragment)

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}