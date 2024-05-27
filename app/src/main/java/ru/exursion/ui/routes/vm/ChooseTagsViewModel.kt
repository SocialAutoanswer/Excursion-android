package ru.exursion.ui.routes.vm

import androidx.paging.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Tag
import ru.exursion.domain.TagsUseCase
import javax.inject.Inject

class ChooseTagsViewModel @Inject constructor(
    private val tagsUseCase: TagsUseCase,
) : RxStateViewModel<ChooseTagsViewModel.ChooseTagsState, Effect>() {

    fun requestTags() = invokeDisposable {
        tagsUseCase.getTags()
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
        data class TagsReady(val tags: PagingData<Tag>) : ChooseTagsState()
    }

    sealed class ChooseTagsEffect : Effect {
        object Error : ChooseTagsEffect()
    }
}