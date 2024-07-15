package ru.exursion.ui.routes.vm

import androidx.paging.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.bibaboba.kit.Logger
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Route
import ru.exursion.domain.RoutesUseCase
import javax.inject.Inject

class RoutesViewModel @Inject constructor(
    private val routesUseCase: RoutesUseCase,
) : RxStateViewModel<RoutesViewModel.RoutesState, RoutesViewModel.RoutesEffect>() {

    fun getRoutesByCityId(cityId: Long, tagId: Int) = invokeDisposable {
        routesUseCase.getRoutesByCity(cityId, tagId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.postValue(RoutesState.Loading) }
            .subscribe ({
                _state.postValue(RoutesState.Ready(it))
            }, {
                Logger.error(RoutesViewModel::class, it)
            })
    }

    sealed class RoutesState : State {
        object Loading : RoutesState()
        class Ready(val routes: PagingData<Route>) : RoutesState()
    }

    sealed class RoutesEffect : Effect
}