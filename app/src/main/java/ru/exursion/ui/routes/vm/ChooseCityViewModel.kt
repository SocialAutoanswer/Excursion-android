package ru.exursion.ui.routes.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.City
import ru.exursion.domain.CitiesUseCase
import javax.inject.Inject

class ChooseCityViewModel @Inject constructor(
    private val citiesUseCase: CitiesUseCase,
) : RxStateViewModel<ChooseCityViewModel.ChooseCityState, ChooseCityViewModel.ChooseCityEffect>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCities() = invokeDisposable {
        citiesUseCase.getCities()
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .doOnSubscribe { _state.postValue(ChooseCityState.Loading) }
            .subscribe({
                _state.postValue(ChooseCityState.Ready(it))
            }, {
                _effect.postValue(ChooseCityEffect.Error)
            })
    }


    sealed class ChooseCityState: State {
        data object Loading : ChooseCityState()
        data class Ready(val citiesData: PagingData<City>) : ChooseCityState()
    }

    sealed class ChooseCityEffect: Effect {
        data object Error: ChooseCityEffect()
    }
}