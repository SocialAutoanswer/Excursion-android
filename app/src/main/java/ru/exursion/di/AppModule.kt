package ru.exursion.di

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
import ru.exursion.data.locations.TagsMapper
import ru.exursion.data.models.City
import ru.exursion.data.models.CityDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.domain.CitiesUseCase
import ru.exursion.domain.CitiesUseCaseImpl
import ru.exursion.ui.auth.AuthViewModel
import ru.exursion.domain.TagsUseCase
import ru.exursion.domain.TagsUseCaseImpl
import ru.exursion.ui.routes.vm.ChooseCityViewModel
import ru.exursion.ui.routes.vm.ChooseTagsViewModel

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideApplicationContext() = context

    @Module
    interface Bind {


        @Binds
        fun bindsTagsMapper(impl: TagsMapper): Mapper<TagDto, Tag>
        @Binds
        fun bindCitiesMapper(impl: CitiesMapper): Mapper<CityDto, City>
        @Binds
        fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository
        @Binds
        fun bindTagsUseCase(impl: TagsUseCaseImpl): TagsUseCase
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
        @ViewModelKey(AuthViewModel::class)
        fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(ChooseTagsViewModel::class)
        fun bindTownRouteTypesViewModel(viewModel: ChooseTagsViewModel): ViewModel
    }

}