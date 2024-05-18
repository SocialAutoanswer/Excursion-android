package ru.exursion

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.bibaboba.kit.dagger.ViewModelFactory
import ru.bibaboba.kit.dagger.ViewModelKey
import ru.exursion.routes.RouteTypesUseCase
import ru.exursion.routes.TownsUseCase
import ru.exursion.routes.vm.ChooseTownViewModel
import ru.exursion.routes.vm.TownRouteTypesViewModel

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideApplicationContext() = context

    @Provides
    fun provideTownsUseCase(): TownsUseCase = TownsUseCase()

    @Provides
    fun provideRouteTypesUseCase(): RouteTypesUseCase = RouteTypesUseCase()

    @Module
    interface Bind {

        @Binds
        fun bindViewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory

        @Binds
        @IntoMap
        @ViewModelKey(ChooseTownViewModel::class)
        fun bindChooseTownsViewModel(viewModel: ChooseTownViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(TownRouteTypesViewModel::class)
        fun bindTownRouteTypesViewModel(viewModel: TownRouteTypesViewModel): ViewModel
    }

}