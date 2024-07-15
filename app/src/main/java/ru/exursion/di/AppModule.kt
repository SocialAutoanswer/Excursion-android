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
import ru.exursion.data.auth.AuthRepository
import ru.exursion.data.auth.AuthRepositoryImpl
import ru.exursion.data.auth.UserMapper
import ru.exursion.data.locations.mapper.CitiesMapper
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.locations.LocationsRepositoryImpl
import ru.exursion.data.locations.mapper.LocationsMapper
import ru.exursion.data.locations.mapper.RouteDetailsMapper
import ru.exursion.data.locations.mapper.RoutesMapper
import ru.exursion.data.locations.mapper.TagsMapper
import ru.exursion.data.models.City
import ru.exursion.data.models.CityDto
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.RouteDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.User
import ru.exursion.data.models.UserRequestDto
import ru.exursion.data.profile.ProfileRepository
import ru.exursion.data.profile.ProfileRepositoryImpl
import ru.exursion.domain.AuthUseCase
import ru.exursion.domain.AuthUseCaseImpl
import ru.exursion.domain.ProfileUseCase
import ru.exursion.domain.ProfileUseCaseImpl
import ru.exursion.data.reviews.ReviewsRepository
import ru.exursion.data.reviews.ReviewsRepositoryImpl
import ru.exursion.data.reviews.mapper.ReviewMapper
import ru.exursion.domain.CitiesUseCase
import ru.exursion.domain.CitiesUseCaseImpl
import ru.exursion.domain.RoutesUseCase
import ru.exursion.domain.RoutesUseCaseImpl
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.domain.TagsUseCase
import ru.exursion.domain.TagsUseCaseImpl
import ru.exursion.ui.SplashViewModel
import ru.exursion.ui.routes.vm.ChooseCityViewModel
import ru.exursion.ui.routes.vm.ChooseTagsViewModel
import ru.exursion.ui.routes.vm.RoutesViewModel

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
        fun bindRoutesMapper(impl: RoutesMapper): Mapper<RouteDto, Route>
        @Binds
        fun bindLocationMapper(impl: LocationsMapper): Mapper<LocationDto, Location>
        @Binds
        fun bindRouteDetailsMapper(impl: RouteDetailsMapper): Mapper<RouteDetailsDto, RouteDetails>
        @Binds
        fun bindReviewMapper(impl: ReviewMapper): Mapper<ReviewDto, Review>

        @Binds
        fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository
        @Binds
        fun bindReviesRepository(impl: ReviewsRepositoryImpl): ReviewsRepository

        @Binds
        fun bindTagsUseCase(impl: TagsUseCaseImpl): TagsUseCase
        @Binds
        fun bindCitiesUseCase(impl: CitiesUseCaseImpl): CitiesUseCase
        @Binds
        fun bindRoutesUseCase(impl: RoutesUseCaseImpl): RoutesUseCase

        @Binds
        fun bindUserMapper(impl: UserMapper): Mapper<UserRequestDto, User>
        @Binds
        fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

        @Binds
        fun bindAuthUseCase(impl: AuthUseCaseImpl): AuthUseCase

        @Binds
        fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

        @Binds
        fun bindProfileUseCase(impl: ProfileUseCaseImpl): ProfileUseCase

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

        @Binds
        @IntoMap
        @ViewModelKey(SplashViewModel::class)
        fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
      
        @Binds
        @IntoMap
        @ViewModelKey(RoutesViewModel::class)
        fun bindRoutesViewModel(viewModel: RoutesViewModel): ViewModel
    }

}