package ru.exursion.routes.vm

import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State

class TownRouteTypesViewModel : RxStateViewModel<TownRouteTypesViewModel.RoutesState, Effect>() {

    sealed class RoutesState : State {
        data object Loading: RoutesState()
        //data class Ready(val routes: List<>): RoutesState()
    }
}