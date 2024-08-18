package ru.exursion.ui.routes.vm

import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.AudioLocation
import ru.exursion.domain.FavoritesUseCase
import ru.exursion.domain.LocationUseCase
import javax.inject.Inject

class LocationDetailsViewModel@Inject constructor(
    private val locationsUseCase: LocationUseCase,
    private val favoritesUseCase: FavoritesUseCase
) : RxStateViewModel<LocationDetailsViewModel.LocationDetailsState, LocationDetailsViewModel.LocationDetailsEffect>() {

    var locationId: Long? = null

    fun getLocationById() = invokeDisposable {
        locationsUseCase.getLocationById(locationId ?: -1)
            .doOnSubscribe { _state.postValue(LocationDetailsState.Loading) }
            .subscribe({
                _state.postValue(LocationDetailsState.Ready(it))
            }, {
                _effect.postValue(LocationDetailsEffect.Error)
            })
    }

    fun changeLocationFavoriteState(locationId: Long) = invokeDisposable {
        favoritesUseCase.changeLocationFavoriteState(locationId)
            .doOnSubscribe{ _state.postValue(LocationDetailsState.LikeLoading) }
            .subscribe({
                _state.postValue(LocationDetailsState.FavoriteStateChanged(it.content))
            }, {
                _effect.postValue(LocationDetailsEffect.Error)
                _state.postValue(LocationDetailsState.Idle)
            })
    }

    sealed class LocationDetailsState: State {
        data object Idle: LocationDetailsState()
        data object Loading: LocationDetailsState()
        data object LikeLoading : LocationDetailsState()
        class Ready(val details: AudioLocation): LocationDetailsState()
        data class FavoriteStateChanged(val message: String): LocationDetailsState()
    }

    sealed class LocationDetailsEffect: Effect {
        data object Error: LocationDetailsEffect()
    }

}