package ru.exursion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.bibaboba.kit.dagger.ViewModelFactory
import ru.bibaboba.kit.dagger.ViewModelKey
import ru.exursion.routes.TownsUseCase
import ru.exursion.routes.vm.ChooseTownViewModel

@Module
class AppModule {

    @Provides
    fun provideTownsUseCase(): TownsUseCase = TownsUseCase()

    @Module
    interface Bind {

        @Binds
        fun bindViewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory

        @Binds
        @IntoMap
        @ViewModelKey(ChooseTownViewModel::class)
        fun bindChooseTownsViewModel(viewModel: ChooseTownViewModel): ViewModel
    }

}