package ru.exursion.ui.routes.vm

import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import javax.inject.Inject

class RoutesViewModel @Inject constructor(

) : RxStateViewModel<RoutesViewModel.RoutesState, RoutesViewModel.RoutesEffect>() {



    sealed class RoutesState : State {

    }

    sealed class RoutesEffect : Effect {

    }
}