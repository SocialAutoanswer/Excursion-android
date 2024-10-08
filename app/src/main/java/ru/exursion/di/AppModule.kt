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
import ru.exursion.data.locations.EventRepository
import ru.exursion.data.locations.EventRepositoryImpl
import ru.exursion.data.locations.FavoritesRepository
import ru.exursion.data.locations.FavoritesRepositoryImpl
import ru.exursion.data.locations.HotelsRepository
import ru.exursion.data.locations.HotelsRepositoryImpl
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.locations.LocationsRepositoryImpl
import ru.exursion.data.locations.mapper.AudioLocationMapper
import ru.exursion.data.locations.mapper.AudioMapper
import ru.exursion.data.locations.mapper.CitiesMapper
import ru.exursion.data.locations.mapper.EventMapper
import ru.exursion.data.locations.mapper.HotelMapper
import ru.exursion.data.locations.mapper.LocationsMapper
import ru.exursion.data.locations.mapper.PhotoMapper
import ru.exursion.data.locations.mapper.SocialMediaMapper
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
import ru.exursion.data.models.Message
import ru.exursion.data.models.MessageDto
import ru.exursion.data.models.MessageMapper
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
import ru.exursion.data.models.Shop
import ru.exursion.data.models.ShopDto
import ru.exursion.data.models.SocialMedia
import ru.exursion.data.models.SocialMediaDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.User
import ru.exursion.data.models.UserDto
import ru.exursion.data.models.UserRequestDto
import ru.exursion.data.profile.ProfileRepository
import ru.exursion.data.profile.ProfileRepositoryImpl
import ru.exursion.data.profile.QuestionMapper
import ru.exursion.data.recommendations.RecommendationsRepository
import ru.exursion.data.recommendations.RecommendationsRepositoryImpl
import ru.exursion.data.recommendations.ShopsMapper
import ru.exursion.data.routes.mapper.ReviewMapper
import ru.exursion.data.routes.RoutesRepository
import ru.exursion.data.routes.RoutesRepositoryImpl
import ru.exursion.data.routes.mapper.RouteDetailsMapper
import ru.exursion.data.routes.mapper.RoutesMapper
import ru.exursion.domain.AuthUseCase
import ru.exursion.domain.AuthUseCaseImpl
import ru.exursion.domain.CitiesUseCase
import ru.exursion.domain.CitiesUseCaseImpl
import ru.exursion.domain.EventsUseCase
import ru.exursion.domain.EventsUseCaseImpl
import ru.exursion.domain.FavoritesUseCase
import ru.exursion.domain.FavoritesUseCaseImpl
import ru.exursion.domain.HotelsUseCase
import ru.exursion.domain.HotelsUseCaseImpl
import ru.exursion.domain.LocationUseCase
import ru.exursion.domain.LocationUseCaseImpl
import ru.exursion.domain.ProfileUseCase
import ru.exursion.domain.ProfileUseCaseImpl
import ru.exursion.domain.RoutesUseCase
import ru.exursion.domain.RoutesUseCaseImpl
import ru.exursion.domain.RecommendationsUseCase
import ru.exursion.domain.RecommendationsUseCaseImpl
import ru.exursion.domain.player.impl.PlayerManagerImpl
import ru.exursion.domain.player.interfaces.PlayerManager
import ru.exursion.ui.SplashViewModel
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.ui.events.ShopsViewModel
import ru.exursion.ui.map.MapViewModel
import ru.exursion.ui.profile.ProfileViewModel
import ru.exursion.ui.routes.vm.ChooseCityViewModel
import ru.exursion.ui.routes.vm.EventDetailsViewModel
import ru.exursion.ui.routes.vm.HotelDetailsViewModel
import ru.exursion.ui.routes.vm.LocationDetailsViewModel
import ru.exursion.ui.routes.vm.RouteDetailsViewModel
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
        fun bindMessageMapper(impl: MessageMapper): Mapper<MessageDto, Message>
        @Binds
        fun bindShopsMapper(impl: ShopsMapper): Mapper<ShopDto, Shop>

        @Binds
        fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository
        @Binds
        fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
        @Binds
        fun bindRoutesRepository(impl: RoutesRepositoryImpl): RoutesRepository
        @Binds
        fun bindRecommendationsRepository(impl: RecommendationsRepositoryImpl): RecommendationsRepository
        @Binds
        fun bindEventsRepository(impl: EventRepositoryImpl): EventRepository
        @Binds
        fun bindHotelsRepository(impl: HotelsRepositoryImpl): HotelsRepository

        @Binds
        fun bindCitiesUseCase(impl: CitiesUseCaseImpl): CitiesUseCase
        @Binds
        fun bindRoutesUseCase(impl: RoutesUseCaseImpl): RoutesUseCase
        @Binds
        fun bindLocationUseCase(impl: LocationUseCaseImpl): LocationUseCase
        @Binds
        fun bindFavoriteUseCase(impl: FavoritesUseCaseImpl): FavoritesUseCase
        @Binds
        fun bindRecommendationsUseCase(impl: RecommendationsUseCaseImpl): RecommendationsUseCase
        @Binds
        fun bindEventsUseCase(impl: EventsUseCaseImpl): EventsUseCase
        @Binds
        fun bindHotelsUseCase(impl: HotelsUseCaseImpl): HotelsUseCase

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
        @ViewModelKey(SplashViewModel::class)
        fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

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

        @Binds
        @IntoMap
        @ViewModelKey(EventDetailsViewModel::class)
        fun bindEventDetailsViewModel(viewModel: EventDetailsViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(HotelDetailsViewModel::class)
        fun bindHotelDetailsViewModel(viewModel: HotelDetailsViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(LocationDetailsViewModel::class)
        fun bindLocationDetailsViewModel(viewModel: LocationDetailsViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(ShopsViewModel::class)
        fun bindShopsViewModel(viewModel: ShopsViewModel): ViewModel
    }

}