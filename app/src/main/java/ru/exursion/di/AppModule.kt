package ru.exursion.di

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
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
import ru.exursion.data.auth.UserRequestMapper
import ru.exursion.data.locations.FavoritesRepository
import ru.exursion.data.locations.FavoritesRepositoryImpl
import ru.exursion.data.locations.mapper.CitiesMapper
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.locations.LocationsRepositoryImpl
import ru.exursion.data.locations.mapper.AudioLocationMapper
import ru.exursion.data.locations.mapper.AudioMapper
import ru.exursion.data.locations.mapper.EventMapper
import ru.exursion.data.locations.mapper.HotelMapper
import ru.exursion.data.locations.mapper.LocationsMapper
import ru.exursion.data.locations.mapper.PhotoMapper
import ru.exursion.data.locations.mapper.RouteDetailsMapper
import ru.exursion.data.locations.mapper.RoutesMapper
import ru.exursion.data.locations.mapper.SocialMediaMapper
import ru.exursion.data.locations.mapper.TagsItemMapper
import ru.exursion.data.locations.mapper.TagsMapper
import ru.exursion.data.models.Audio
import ru.exursion.data.models.AudioDto
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.AudioLocationDto
import ru.exursion.data.models.City
import ru.exursion.data.models.CityDto
import ru.exursion.data.models.Event
import ru.exursion.data.models.EventDto
import ru.exursion.data.models.Hotel
import ru.exursion.data.models.HotelDto
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.Photo
import ru.exursion.data.models.PhotoDto
import ru.exursion.data.models.Question
import ru.exursion.data.models.QuestionDto
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.RouteDto
import ru.exursion.data.models.SocialMedia
import ru.exursion.data.models.SocialMediaDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.TagItem
import ru.exursion.data.models.TagItemDto
import ru.exursion.data.models.User
import ru.exursion.data.models.UserDto
import ru.exursion.data.models.UserRequestDto
import ru.exursion.data.profile.ProfileRepository
import ru.exursion.data.profile.ProfileRepositoryImpl
import ru.exursion.data.profile.QuestionMapper
import ru.exursion.domain.AuthUseCase
import ru.exursion.domain.AuthUseCaseImpl
import ru.exursion.domain.ProfileUseCase
import ru.exursion.domain.ProfileUseCaseImpl
import ru.exursion.data.reviews.ReviewsRepository
import ru.exursion.data.reviews.ReviewsRepositoryImpl
import ru.exursion.data.reviews.mapper.ReviewMapper
import ru.exursion.domain.CitiesUseCase
import ru.exursion.domain.CitiesUseCaseImpl
import ru.exursion.domain.ContentUseCase
import ru.exursion.domain.ContentUseCaseImpl
import ru.exursion.domain.FavoritesUseCase
import ru.exursion.domain.FavoritesUseCaseImpl
import ru.exursion.domain.LocationUseCase
import ru.exursion.domain.LocationUseCaseImpl
import ru.exursion.domain.RoutesUseCase
import ru.exursion.domain.RoutesUseCaseImpl
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.domain.TagsUseCase
import ru.exursion.domain.TagsUseCaseImpl
import ru.exursion.domain.player.impl.PlayerManagerImpl
import ru.exursion.domain.player.interfaces.PlayerManager
import ru.exursion.ui.SplashViewModel
import ru.exursion.ui.map.MapViewModel
import ru.exursion.ui.profile.ProfileViewModel
import ru.exursion.ui.routes.vm.ChooseCityViewModel
import ru.exursion.ui.routes.vm.ChooseTagsViewModel
import ru.exursion.ui.routes.vm.RouteDetailsViewModel
import ru.exursion.ui.routes.vm.RoutesViewModel
import ru.exursion.ui.shared.content.BaseContentViewModel

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideApplicationContext() = context

    @Provides
    fun provideAudioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

    @Provides
    fun provideMediaPlayer(audioAttributes: AudioAttributes) = MediaPlayer().apply {
        setAudioAttributes(audioAttributes)
    }


    @Provides
    fun provideAudioFocusBuilder(audioAttributes: AudioAttributes) : AudioFocusRequest.Builder =
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(audioAttributes)
            .setAcceptsDelayedFocusGain(true)

    @Provides
    fun providePlayerManager() : PlayerManager = PlayerManagerImpl(context)

    @Module
    interface Bind {

        @Binds
        fun bindsTagsMapper(impl: TagsMapper): Mapper<TagDto, Tag>
        @Binds
        fun bindsTagsItemMapper(impl: TagsItemMapper): Mapper<TagItemDto, TagItem>
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
        fun bindQuestionMapper(impl: QuestionMapper): Mapper<QuestionDto, Question>
        @Binds
        fun bindUserMapper(impl: UserMapper): Mapper<UserDto, User>
        @Binds
        fun bindPhotoMapper(impl: PhotoMapper): Mapper<PhotoDto, Photo>
        @Binds
        fun bindAudioMapper(impl: AudioMapper): Mapper<AudioDto, Audio>
        @Binds
        fun bindAudioLocationMapper(impl: AudioLocationMapper): Mapper<AudioLocationDto, AudioLocation>
        @Binds
        fun bindHotelMapper(impl: HotelMapper): Mapper<HotelDto, Hotel>
        @Binds
        fun bindSocialMediaMapper(impl: SocialMediaMapper): Mapper<SocialMediaDto, SocialMedia>
        @Binds
        fun bindEventMapper(impl: EventMapper): Mapper<EventDto, Event>

        @Binds
        fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository
        @Binds
        fun bindReviesRepository(impl: ReviewsRepositoryImpl): ReviewsRepository
        @Binds
        fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

        @Binds
        fun bindTagsUseCase(impl: TagsUseCaseImpl): TagsUseCase
        @Binds
        fun bindCitiesUseCase(impl: CitiesUseCaseImpl): CitiesUseCase
        @Binds
        fun bindRoutesUseCase(impl: RoutesUseCaseImpl): RoutesUseCase
        @Binds
        fun bindLocationUseCase(impl: LocationUseCaseImpl): LocationUseCase
        @Binds
        fun bindContentUseCase(impl: ContentUseCaseImpl): ContentUseCase
        @Binds
        fun bindFavoriteUseCase(impl: FavoritesUseCaseImpl): FavoritesUseCase

        @Binds
        fun bindUserRequestMapper(impl: UserRequestMapper): Mapper<UserRequestDto, User>
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

        @Binds
        @IntoMap
        @ViewModelKey(ProfileViewModel::class)
        fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(MapViewModel::class)
        fun bindMapViewModel(viewModel: MapViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(RouteDetailsViewModel::class)
        fun bindRouteDetailsViewModel(viewModel: RouteDetailsViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(BaseContentViewModel::class)
        fun bindBaseContentViewModel(viewModel: BaseContentViewModel): ViewModel
    }

}