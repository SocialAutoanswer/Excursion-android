package ru.exursion

import dagger.Component
import ru.exursion.data.NetworkModule
import ru.exursion.routes.ui.ChooseCityFragment
import ru.exursion.routes.ui.TownRouteTypesFragment
import ru.exursion.settings.SettingsModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    AppModule.Bind::class,
    SettingsModule::class,
    NetworkModule::class,
])
interface AppComponent {

    fun inject(fragment: ChooseCityFragment)
    fun inject(fragment: TownRouteTypesFragment)

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}