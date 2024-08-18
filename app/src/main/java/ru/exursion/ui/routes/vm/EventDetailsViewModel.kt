package ru.exursion.ui.routes.vm

import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Event
import ru.exursion.domain.EventsUseCase
import ru.exursion.domain.FavoritesUseCase
import javax.inject.Inject

class EventDetailsViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase,
    private val favoritesUseCase: FavoritesUseCase
) : RxStateViewModel<EventDetailsViewModel.EventDetailsState, EventDetailsViewModel.EventDetailsEffect>() {

    var eventId: Long? = null

    fun getEventById() = invokeDisposable {
        eventsUseCase.getEventById(eventId ?: -1)
            .doOnSubscribe { _state.postValue(EventDetailsState.Loading) }
            .subscribe({
                _state.postValue(EventDetailsState.Ready(it))
            }, {
                _effect.postValue(EventDetailsEffect.Error)
            })
    }

    fun changeEventFavoriteState(eventId: Long) = invokeDisposable {
        favoritesUseCase.changeEventFavoriteState(eventId)
            .doOnSubscribe{ _state.postValue(EventDetailsState.LikeLoading) }
            .subscribe({
                _state.postValue(EventDetailsState.FavoriteStateChanged(it.content))
            }, {
                _effect.postValue(EventDetailsEffect.Error)
                _state.postValue(EventDetailsState.Idle)
            })
    }

    sealed class EventDetailsState: State {
        data object Idle: EventDetailsState()
        data object Loading: EventDetailsState()
        data object LikeLoading : EventDetailsState()
        class Ready(val details: Event): EventDetailsState()
        data class FavoriteStateChanged(val message: String): EventDetailsState()
    }

    sealed class EventDetailsEffect: Effect {
        data object Error: EventDetailsEffect()
    }
}