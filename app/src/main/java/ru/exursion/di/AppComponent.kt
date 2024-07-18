package ru.exursion.di

import dagger.Component
import ru.exursion.App
import ru.exursion.data.network.NetworkModule
import ru.exursion.ui.routes.fragments.ChooseCityFragment
import ru.exursion.ui.routes.fragments.TagsFragment
import ru.exursion.domain.settings.SettingsModule
import ru.exursion.ui.SplashActivity
import ru.exursion.ui.auth.fragments.EnterCodeFragment
import ru.exursion.ui.auth.fragments.EnterUserDataFragment
import ru.exursion.ui.auth.fragments.SignInFragment
import ru.exursion.ui.auth.fragments.SignUpFragment
import ru.exursion.ui.profile.DecorSettingsFragment
import ru.exursion.ui.profile.ProfileFragment
import ru.exursion.ui.profile.QuestionsFragment
import ru.exursion.ui.profile.RedactProfileFragment
import ru.exursion.ui.routes.fragments.RoutesFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    AppModule.Bind::class,
    SettingsModule::class,
    NetworkModule::class,
])
interface AppComponent {

    fun inject(application: App)

    fun inject(activity: SplashActivity)

    fun inject(fragment: ChooseCityFragment)
    fun inject(fragment: TagsFragment)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: EnterCodeFragment)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: EnterUserDataFragment)
    fun inject(fragment: RoutesFragment)
    fun inject(fragment: DecorSettingsFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: QuestionsFragment)
    fun inject(fragment: RedactProfileFragment)

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}