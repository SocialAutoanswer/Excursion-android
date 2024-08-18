package ru.exursion.di

import dagger.Component
import ru.exursion.App
import ru.exursion.data.network.NetworkModule
import ru.exursion.domain.player.MapPlayer
import ru.exursion.domain.player.MediaPlayerService
import ru.exursion.ui.routes.fragments.ChooseCityFragment
import ru.exursion.ui.routes.fragments.TagsFragment
import ru.exursion.domain.settings.SettingsModule
import ru.exursion.ui.SplashActivity
import ru.exursion.ui.auth.fragments.EnterCodeFragment
import ru.exursion.ui.auth.fragments.EnterUserDataFragment
import ru.exursion.ui.auth.fragments.SignInFragment
import ru.exursion.ui.auth.fragments.SignUpFragment
import ru.exursion.ui.events.RecommendationFragment
import ru.exursion.ui.map.LocationBottomDialog
import ru.exursion.ui.map.MapFragment
import ru.exursion.ui.profile.DecorSettingsFragment
import ru.exursion.ui.profile.ProfileFragment
import ru.exursion.ui.profile.QuestionsFragment
import ru.exursion.ui.profile.RedactProfileFragment
import ru.exursion.ui.routes.fragments.EventDetailsFragment
import ru.exursion.ui.routes.fragments.EventsFragment
import ru.exursion.ui.routes.fragments.HotelDetailsFragment
import ru.exursion.ui.routes.fragments.HotelsFragment
import ru.exursion.ui.routes.fragments.LocationDetailsFragment
import ru.exursion.ui.routes.fragments.LocationsFragment
import ru.exursion.ui.routes.fragments.ReviewDialogFragment
import ru.exursion.ui.routes.fragments.ReviewsFragment
import ru.exursion.ui.routes.fragments.RouteAudiosDialog
import ru.exursion.ui.routes.fragments.RouteDetailsFragment
import ru.exursion.ui.routes.fragments.RouteMapFragment
import ru.exursion.ui.routes.fragments.RoutesFragment
import ru.exursion.ui.shared.content.BaseContentFragment
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
    fun inject(fragment: MapFragment)
    fun inject(service: MediaPlayerService)
    fun inject(fragment: LocationBottomDialog)
    fun inject(player: MapPlayer)
    fun inject(fragment: RouteDetailsFragment)
    fun inject(fragment: BaseContentFragment)
    fun inject(fragment: HotelsFragment)
    fun inject(fragment: EventsFragment)
    fun inject(fragment: RouteMapFragment)
    fun inject(fragment: RouteAudiosDialog)
    fun inject(fragment: RecommendationFragment)
    fun inject(fragment: LocationsFragment)
    fun inject(fragment: ReviewsFragment)
    fun inject(fragment: ReviewDialogFragment)
    fun inject(fragment: HotelDetailsFragment)
    fun inject(fragment: EventDetailsFragment)
    fun inject(fragment: LocationDetailsFragment)

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}