package ru.exursion.ui.routes.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagItem
import ru.exursion.domain.TagsUseCase
import javax.inject.Inject

class ChooseTagsViewModel @Inject constructor(
    private val tagsUseCase: TagsUseCase,
) : RxStateViewModel<ChooseTagsViewModel.ChooseTagsState, Effect>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun requestTags() = invokeDisposable {
        tagsUseCase.getTags()
            .cachedIn(viewModelScope)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.postValue(ChooseTagsState.Loading) }
            .subscribe ({
                _state.postValue(ChooseTagsState.TagsReady(it))
            }, {
                _effect.postValue(ChooseTagsEffect.Error)
            })
    }

    sealed class ChooseTagsState : State {
        data object Loading : ChooseTagsState()
        data class TagsReady(val tags: PagingData<TagItem>) : ChooseTagsState()
    }

    sealed class ChooseTagsEffect : Effect {
        object Error : ChooseTagsEffect()
    }
}