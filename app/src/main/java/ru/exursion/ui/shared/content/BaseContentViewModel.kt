package ru.exursion.ui.shared.content

import io.reactivex.rxjava3.core.Single
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.domain.ContentUseCase
import ru.exursion.domain.FavoritesUseCase
import javax.inject.Inject

class BaseContentViewModel @Inject constructor(
    private val contentUseCase: ContentUseCase,
    private val favoritesUseCase: FavoritesUseCase
) : RxStateViewModel<BaseContentViewModel.ContentState, BaseContentViewModel.ContentEffect>() {

    var cityId: Long? = null
    var tagId: Long? = null
    var isFavorite: Boolean = false

    init {
        _state.postValue(ContentState.Idle)
    }

    private fun <D: Any> getData(method: Single<List<D>>) = invokeDisposable {
        method
            .doOnSubscribe { _state.postValue(ContentState.Loading) }
            .subscribe({
                if (it.isEmpty()) {
                    _state.postValue(ContentState.Idle)
                    return@subscribe
                }

                _state.postValue(ContentState.Ready(it))
            }, {
                _effect.postValue(ContentEffect.Error)
                _state.postValue(ContentState.Idle)
            })
    }

    fun getRoutes() {
        if (cityId == null || tagId == null) {
            _effect.postValue(ContentEffect.Error)
            return
        }

        getData(
            contentUseCase.getRoutesByCity(cityId ?: -1, tagId ?: -1)
        )
    }

    fun getFavoriteRoutes() = getData(
        favoritesUseCase.getFavoriteRoutes()
    )

    fun getLocations() = cityId?.let{
        getData(
            contentUseCase.getLocationsByCity(it)
        )
    } ?: {
        _effect.postValue(ContentEffect.Error)
    }

    fun getFavoriteLocations() = getData(
        favoritesUseCase.getFavoriteLocations()
    )

    fun getFavoriteHotels() = getData(
        favoritesUseCase.getFavoriteHotels()
    )

    fun getFavoriteEvents() = getData(
        favoritesUseCase.getFavoriteEvents()
    )

    sealed class ContentState : State {
        data object Idle : ContentState()
        data object Loading : ContentState()
        class Ready(val content: List<Any>) : ContentState()
    }

    sealed class ContentEffect : Effect {
        data object Error : ContentEffect()
    }
}