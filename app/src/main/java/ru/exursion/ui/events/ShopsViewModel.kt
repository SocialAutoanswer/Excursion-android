package ru.exursion.ui.events

import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Shop
import ru.exursion.domain.RecommendationsUseCase
import javax.inject.Inject

class ShopsViewModel @Inject constructor(
    private val recommendationsUseCase: RecommendationsUseCase,
): RxStateViewModel<ShopsViewModel.ContentState, ShopsViewModel.ContentEffect>() {

    fun getShops(tagId: Long) = invokeDisposable {
        recommendationsUseCase.getShops(tagId)
            .doOnSubscribe { _state.postValue(ContentState.Loading) }
            .subscribe({
                _state.postValue(ContentState.Ready(it))
            }, {
                _effect.postValue(ContentEffect.Error)
                _state.postValue(ContentState.Idle)
            })
    }

    sealed class ContentState : State {
        data object Idle : ContentState()
        data object Loading : ContentState()
        class Ready(val content: List<Shop>) : ContentState()
    }

    sealed class ContentEffect : Effect {
        data object Error : ContentEffect()
    }
}