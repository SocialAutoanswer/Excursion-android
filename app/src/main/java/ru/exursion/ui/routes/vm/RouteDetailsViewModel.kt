package ru.exursion.ui.routes.vm

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.RouteDetailsWithReview
import ru.exursion.domain.RoutesUseCase
import javax.inject.Inject

class RouteDetailsViewModel @Inject constructor(
    private val routesUseCase: RoutesUseCase,
) : RxStateViewModel<RouteDetailsViewModel.RouteDetailsState, RouteDetailsViewModel.RouteDetailsEffect>() {

    fun getRouteDetails(routeId: Long, tagId: Long) = invokeDisposable {
        routesUseCase.getRouteDetailsWithComments(routeId, tagId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.postValue(RouteDetailsState.Loading) }
            .subscribe({
                _state.postValue(RouteDetailsState.Ready(it))
            }, {
                _effect.postValue(RouteDetailsEffect.Error)
            })
    }

    sealed class RouteDetailsState : State {
        object Loading : RouteDetailsState()
        data class Ready(val details: RouteDetailsWithReview) : RouteDetailsState()
    }

    sealed class RouteDetailsEffect : Effect {
        object Error : RouteDetailsEffect()
    }
}