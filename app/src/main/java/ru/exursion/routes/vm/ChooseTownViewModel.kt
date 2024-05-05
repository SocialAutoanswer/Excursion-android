package ru.exursion.routes.vm

import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.routes.TownItem
import ru.exursion.routes.TownsUseCase
import javax.inject.Inject

class ChooseTownViewModel @Inject constructor(
    private val townsUseCase: TownsUseCase,
) : RxStateViewModel<ChooseTownViewModel.ChooseTownState, ChooseTownViewModel.ChooseTownEffect>() {

    fun requestTownsIfNeeded() {
        if (state.value is ChooseTownState.Ready) return

        requestTowns()
    }

    fun requestTowns() = invokeDisposable {
        townsUseCase.getTowns()
            .doOnSubscribe { _state.postValue(ChooseTownState.Loading) }
            .subscribe({
                _state.postValue(ChooseTownState.Ready(it))
            }, {
                _effect.postValue(ChooseTownEffect.Error)
            })
    }

    sealed class ChooseTownState: State {
        data object Loading : ChooseTownState()
        data class Ready(val towns: List<TownItem>) : ChooseTownState()
    }

    sealed class ChooseTownEffect: Effect {
        data object Error: ChooseTownEffect()
    }
}