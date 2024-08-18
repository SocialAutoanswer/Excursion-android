package ru.exursion.ui.routes.vm

import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Hotel
import ru.exursion.domain.FavoritesUseCase
import ru.exursion.domain.HotelsUseCase
import javax.inject.Inject

class HotelDetailsViewModel @Inject constructor(
    private val hotelsUseCase: HotelsUseCase,
    private val favoritesUseCase: FavoritesUseCase
) : RxStateViewModel<HotelDetailsViewModel.HotelDetailsState, HotelDetailsViewModel.HotelDetailsEffect>() {

    var hotelId: Long? = null

    fun getHotelById() = invokeDisposable {
        hotelsUseCase.getHotelById(hotelId ?: -1)
            .doOnSubscribe { _state.postValue(HotelDetailsState.Loading) }
            .subscribe({
                _state.postValue(HotelDetailsState.Ready(it))
            }, {
                _effect.postValue(HotelDetailsEffect.Error)
            })
    }

    fun changeHotelFavoriteState(hotelId: Long) = invokeDisposable {
        favoritesUseCase.changeHotelFavoriteState(hotelId)
            .doOnSubscribe{ _state.postValue(HotelDetailsState.LikeLoading) }
            .subscribe({
                _state.postValue(HotelDetailsState.FavoriteStateChanged(it.content))
            }, {
                _effect.postValue(HotelDetailsEffect.Error)
                _state.postValue(HotelDetailsState.Idle)
            })
    }

    sealed class HotelDetailsState: State {
        data object Idle: HotelDetailsState()
        data object Loading: HotelDetailsState()
        data object LikeLoading : HotelDetailsState()
        class Ready(val details: Hotel): HotelDetailsState()
        data class FavoriteStateChanged(val message: String): HotelDetailsState()
    }

    sealed class HotelDetailsEffect: Effect {
        data object Error: HotelDetailsEffect()
    }
}