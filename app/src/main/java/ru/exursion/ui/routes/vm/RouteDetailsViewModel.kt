package ru.exursion.ui.routes.vm

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yandex.mapkit.geometry.Point
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.SingleLiveEvent
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Audio
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.RouteDetails
import ru.exursion.domain.FavoritesUseCase
import ru.exursion.domain.RoutesUseCase
import ru.exursion.domain.player.RoutePlayer
import javax.inject.Inject

class RouteDetailsViewModel @Inject constructor(
    private val routesUseCase: RoutesUseCase,
    private val favoritesUseCase: FavoritesUseCase,
    val routePlayer: RoutePlayer
) : RxStateViewModel<RouteDetailsViewModel.RouteDetailsState, RouteDetailsViewModel.RouteDetailsEffect>() {

    var routeName: String? = null
        private set
    var routeAudios: List<AudioLocation> = emptyList()
        private set

    private val _reviewPosted = SingleLiveEvent<Boolean?>()
    val reviewPosted: LiveData<Boolean?> = _reviewPosted

    fun getIsSomeonePlaying() = routePlayer.isSomeonePlaying

    fun getCurrentPlayingAudio() = routeAudios.find { it.id == routePlayer.currentPlayingTrackId }?.audios?.get(0)

    fun setOnPlayerTimerListener(callback: (Int) -> Unit) = invokeDisposable {
        routePlayer.observePlayerTimer()
            .subscribe({
                callback(it)
            }, {
                _effect.postValue(RouteDetailsEffect.Error)
            })
    }

    fun getRouteDetails(routeId: Long) = invokeDisposable {
        routesUseCase.getRouteById(routeId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.postValue(RouteDetailsState.Loading) }
            .subscribe({
                routeName = it.name
                routeAudios = it.locations
                _state.postValue(RouteDetailsState.Ready(it))
            }, {
                _effect.postValue(RouteDetailsEffect.Error)
            })
    }

    fun changeRouteFavoriteState(routeId: Long) = invokeDisposable {
        favoritesUseCase.changeRouteFavoriteState(routeId)
            .doOnSubscribe{ _state.postValue(RouteDetailsState.LikeLoading) }
            .subscribe({
                _state.postValue(RouteDetailsState.FavoriteStateChanged(it.content))
            }, {
                _effect.postValue(RouteDetailsEffect.Error)
                _state.postValue(RouteDetailsState.Idle)
            })
    }

    fun addReview(routeId: Long, @IntRange(1, 5) rating: Int, reviewText: String) {
        routesUseCase.postReview(routeId, rating, reviewText)
            .subscribe({
                _reviewPosted.postValue(true)
            }, {
                if (it is IllegalArgumentException) {
                    _reviewPosted.postValue(false)
                } else {
                    _reviewPosted.postValue(null)
                }
               _state.postValue(RouteDetailsState.Idle)
            })
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