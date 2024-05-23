package ru.exursion.routes.vm

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.routes.TownItem
import ru.exursion.routes.TownsUseCase
import javax.inject.Inject

class ChooseCityViewModel @Inject constructor(
    private val townsUseCase: TownsUseCase,
) : RxStateViewModel<ChooseCityViewModel.ChooseCityState, ChooseCityViewModel.ChooseCityEffect>() {

    fun requestTownsIfNeeded() {
        if (state.value is ChooseCityState.Ready) return

        requestTowns()
    }

    fun requestTowns() = invokeDisposable {
        townsUseCase.getTowns()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.postValue(ChooseCityState.Loading) }
            .subscribe({
                _state.postValue(ChooseCityState.Ready(it))
            }, {
                _effect.postValue(ChooseCityEffect.Error)
            })
    }

    sealed class ChooseCityState: State {
        data object Loading : ChooseCityState()
        data class Ready(val towns: List<TownItem>) : ChooseCityState()
    }

    sealed class ChooseCityEffect: Effect {
        data object Error: ChooseCityEffect()
    }
}