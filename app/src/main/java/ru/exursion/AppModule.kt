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
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.locations.CitiesMapper
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.locations.LocationsRepositoryImpl
import ru.exursion.data.locations.models.CitiesPageDto
import ru.exursion.data.locations.models.City
import ru.exursion.data.locations.models.CityDto
import ru.exursion.routes.CitiesUseCase
import ru.exursion.routes.CitiesUseCaseImpl
import ru.exursion.routes.RouteTypesUseCase
import ru.exursion.routes.vm.ChooseCityViewModel
import ru.exursion.routes.vm.TownRouteTypesViewModel

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideApplicationContext() = context

    @Provides
    fun provideRouteTypesUseCase(): RouteTypesUseCase = RouteTypesUseCase()

    @Module
    interface Bind {

        @Binds
        fun bindCitiesMapper(impl: CitiesMapper): Mapper<CityDto, City>

        @Binds
        fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository

        @Binds
        fun bindCitiesUseCase(impl: CitiesUseCaseImpl): CitiesUseCase

        @Binds
        fun bindViewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory

        @Binds
        @IntoMap
        @ViewModelKey(ChooseCityViewModel::class)
        fun bindChooseTownsViewModel(viewModel: ChooseCityViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(TownRouteTypesViewModel::class)
        fun bindTownRouteTypesViewModel(viewModel: TownRouteTypesViewModel): ViewModel
    }

}