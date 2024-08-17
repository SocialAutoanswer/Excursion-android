package ru.exursion.ui.routes.vm

import androidx.annotation.IntRange
import com.yandex.mapkit.geometry.Point
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Audio
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.RouteDetails
import ru.exursion.domain.RoutesUseCase
import ru.exursion.domain.player.RoutePlayer
import javax.inject.Inject

class RouteDetailsViewModel @Inject constructor(
    private val routesUseCase: RoutesUseCase,
    val routePlayer: RoutePlayer
) : RxStateViewModel<RouteDetailsViewModel.RouteDetailsState, RouteDetailsViewModel.RouteDetailsEffect>() {

    var routeName: String? = "Маршрут Баумана"   //it is mock data initialize it when route details received
    var routeAudios: List<AudioLocation> = listOf(
        AudioLocation(1, "asd", "qweasd", false, null,
            Point(55.644838, 37.607978),
            listOf(
                Audio(1, "Xnj", "https://killroyka-matchinc-c837.twc1.net/media/LocationAudios/Red_Hot_Chili_Peppers_-_Cant_Stop_Album_Version.mp3", 1),
                Audio(2, "asdqwe", "https://killroyka-matchinc-c837.twc1.net/media/LocationAudios/Red_Hot_Chili_Peppers_-_Cant_Stop_Album_Version.mp3", 1)
            ),
            emptyList()),

        AudioLocation(2, "asd", "qweasd", false, null,
        Point(55.644808, 37.610966),
        listOf(                                                                                        //now it is mock data
            Audio(1, "Xnj", "https://killroyka-matchinc-c837.twc1.net/media/LocationAudios/Red_Hot_Chili_Peppers_-_Snow_Hey_Oh_Album_Version.mp3", 1),                                         //initialize it when route details received
            Audio(2, "asdqwe", "https://killroyka-matchinc-c837.twc1.net/media/LocationAudios/Red_Hot_Chili_Peppers_-_Snow_Hey_Oh_Album_Version.mp3", 1)
        ),
        emptyList())
    )

    fun getIsSomeonePlaying() = routePlayer.isSomeonePlaying

    fun getRouteDetails(routeId: Long) = invokeDisposable {
        routesUseCase.getRouteById(routeId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.postValue(RouteDetailsState.Loading) }
            .subscribe({
                _state.postValue(RouteDetailsState.Ready(it))
            }, {
                _effect.postValue(RouteDetailsEffect.Error)
            })
    }

    fun changeRouteFavoriteState(routeId: Long) = invokeDisposable {
        routesUseCase.changeRouteFavoriteState(routeId)
            .doOnSubscribe{ _state.postValue(RouteDetailsState.LikeLoading) }
            .subscribe({
                _state.postValue(RouteDetailsState.FavoriteStateChanged(it.content))
            }, {
                _effect.postValue(RouteDetailsEffect.Error)
                _state.postValue(RouteDetailsState.Idle)
            })
    }

    fun addReview(@IntRange(1, 5) rating: Int, reviewText: String) {

    }

    sealed class RouteDetailsState : State {
        data object Idle : RouteDetailsState()
        data object Loading : RouteDetailsState()
        data object LikeLoading : RouteDetailsState()
        data class Ready(val details: RouteDetails) : RouteDetailsState()
        data class FavoriteStateChanged(val message: String): RouteDetailsState()
    }

    sealed class RouteDetailsEffect : Effect {
        data object Error : RouteDetailsEffect()
    }
}