package ru.exursion.ui.profile

import androidx.paging.PagingData
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Question
import ru.exursion.data.models.User
import ru.exursion.domain.ProfileUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
): RxStateViewModel<ProfileViewModel.ProfileState, ProfileViewModel.ProfileEffect>() {

    fun getProfile() = invokeDisposable {
        profileUseCase.getProfile()
            .doOnSubscribe { _state.postValue(ProfileState.Loading) }
            .subscribe({ profile ->
                _state.postValue(ProfileState.ProfileReceived(profile))
            }, {
                _effect.postValue(ProfileEffect.Error)
            })
    }

    fun editProfile(user: User) = invokeDisposable {
        profileUseCase.editProfile(user)
            .doOnSubscribe { _state.postValue(ProfileState.Loading) }
            .subscribe({
                    _state.postValue(ProfileState.ProfileEdited)
                }, {
                    _effect.postValue(ProfileEffect.Error)
                    _state.postValue(ProfileState.Idle)
                }
            )
    }

    fun deleteProfile() = invokeDisposable {
        profileUseCase.deleteProfile()
            .doOnSubscribe { _state.postValue(ProfileState.Loading) }
            .subscribe({
                _state.postValue(ProfileState.ProfileDeleted)
            }, {
                _effect.postValue(ProfileEffect.Error)
                _state.postValue(ProfileState.Idle)
            })
    }


    sealed class ProfileState: State {
        data object Idle: ProfileState()
        data object Loading: ProfileState()
        class ProfileReceived(val user: User?) : ProfileState()
        data object ProfileEdited: ProfileState()
        data object ProfileDeleted: ProfileState()
    }

    sealed class ProfileEffect: Effect {
        data object Error: ProfileEffect()
        data object NetworkError: ProfileEffect()
        data object InternalServerError: ProfileEffect()
    }

}