package ru.exursion

import dagger.Component
import ru.exursion.routes.ui.ChooseTownFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AppModule.Bind::class])
interface AppComponent {

    fun inject(fragment: ChooseTownFragment)

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}