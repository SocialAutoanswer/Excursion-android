package ru.exursion.routes.vm

import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.routes.RouteTypesUseCase
import ru.exursion.shared.ui.SelectItem
import javax.inject.Inject

class TownRouteTypesViewModel @Inject constructor(
    private val routeTypesUseCase: RouteTypesUseCase,
) : RxStateViewModel<TownRouteTypesViewModel.RoutesState, Effect>() {

    fun requestTownRouteTypes() = invokeDisposable {
        routeTypesUseCase.getRouteTypes()
            .doOnSubscribe { _state.postValue(RoutesState.Loading) }
            .subscribe({
                _state.postValue(RoutesState.Ready(it))
            }, {
                _state.postValue(RoutesState.NetworkError)
            })
    }

    sealed class RoutesState : State {
        data object Loading : RoutesState()
        data class Ready(val routeTypes: List<SelectItem>) : RoutesState()
        data object NetworkError : RoutesState()
    }
}