package ru.exursion.ui.shared.content

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.bibaboba.kit.Logger
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.domain.FavoritesUseCase
import ru.exursion.domain.LocationUseCase
import ru.exursion.domain.ProfileUseCase
import ru.exursion.domain.RecommendationsUseCase
import ru.exursion.domain.RoutesUseCase
import javax.inject.Inject

class BaseContentViewModel @Inject constructor(
    private val locationsUseCase: LocationUseCase,
    private val routesUseCase: RoutesUseCase,
    private val favoritesUseCase: FavoritesUseCase,
    private val recommendationsUseCase: RecommendationsUseCase,
    private val profileUseCase: ProfileUseCase
) : RxStateViewModel<BaseContentViewModel.ContentState, BaseContentViewModel.ContentEffect>() {

    var cityId: Long? = null
    var tagId: Long? = null
    var tagName: String? = null
    var routeId: Long? = null
    var isFavorite: Boolean = false

    fun getRoutes() = getData(routesUseCase.getRoutes(cityId, tagId))

    fun getFavoriteRoutes() = getData(favoritesUseCase.getFavoriteRoutes())

    fun clearFavoriteRoutes() = clearData(
        favoritesUseCase.clearAllFavoriteRoutes()
    )

    fun getLocations() = getData(locationsUseCase.getLocations(cityId, tagId, routeId))

    fun getFavoriteLocations() = getData(favoritesUseCase.getFavoriteLocations())

    fun clearFavoriteLocations() = clearData(
        favoritesUseCase.clearAllFavoriteLocations()
    )

    fun getFavoriteHotels() = getData(favoritesUseCase.getFavoriteHotels())

    fun clearFavoriteHotels() = clearData(
        favoritesUseCase.clearAllFavoriteHotels()
    )

    fun getFavoriteEvents() = getData(favoritesUseCase.getFavoriteEvents())

    fun clearFavoriteEvents() = clearData(
        favoritesUseCase.clearAllFavoriteEvents()
    )

    fun getRouteTags() = getData(routesUseCase.getRoutesTags())

    fun getRecommendationsTags() = getData(
        recommendationsUseCase.getRecommendationsTags()
    )

    fun getRouteReviews() = routeId?.let {
        getData(routesUseCase.getRouteReviews(it))
    }

    fun getQuestions() = getData(
        profileUseCase.getQuestions()
    )

    fun setIdleState() {
        _state.value = ContentState.Idle
    }

    private fun <D: Any> getData(method: Single<List<D>>) = invokeDisposable {
        method
            .doOnSubscribe { _state.postValue(ContentState.Loading) }
            .subscribe({
                if (it.isEmpty()) {
                    _state.postValue(ContentState.Idle)
                    return@subscribe
                }

                _state.postValue(ContentState.Ready(PagingData.from(it)))
            }, {
                Logger.error(this::class, it)
                _effect.postValue(ContentEffect.Error)
                _state.postValue(ContentState.Idle)
            })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <D: Any> getData(method: Flowable<PagingData<D>>) = invokeDisposable {
        method
            .cachedIn(viewModelScope)
            .subscribe({
                _state.postValue(ContentState.Ready(it as PagingData<Any>))
            }, {
                Logger.error(this::class, it)
                _effect.postValue(ContentEffect.Error)
                _state.postValue(ContentState.Idle)
            })
    }

    private fun clearData(method: Single<Unit>) = invokeDisposable {
        method
            .doOnSubscribe { _state.postValue(ContentState.Loading) }
            .subscribe({
                _state.postValue(ContentState.ContentCleared)
            }, {
                _effect.postValue(ContentEffect.Error)
            })
    }

    sealed class ContentState : State {
        data object Idle : ContentState()
        data object Loading : ContentState()
        class Ready(val content: PagingData<Any>) : ContentState()
        data object ContentCleared : ContentState()
    }

    sealed class ContentEffect : Effect {
        data object Error : ContentEffect()
    }
}