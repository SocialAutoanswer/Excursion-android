package ru.exursion.ui.auth.vm

import android.os.CountDownTimer
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.EmailAlreadyRegistered
import ru.exursion.data.IncorrectCode
import ru.exursion.data.IncorrectPassword
import ru.exursion.data.InternalServerError
import ru.exursion.data.models.BirthDate
import ru.exursion.data.models.User
import ru.exursion.domain.AuthUseCase
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : RxStateViewModel<AuthViewModel.AuthState, AuthViewModel.AuthEffect>() {

    companion object {
        private const val TIMER_FINISH_TIME = 30000L
        private const val TIMER_STEP_INTERVAL = 1000L
    }

    val user = User("", "", BirthDate(-1, -1, -1), "", "", "", "").copy()

    private val countDownTimer = object : CountDownTimer(TIMER_FINISH_TIME, TIMER_STEP_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            timerIsRunning = true
            onTick?.invoke(millisUntilFinished)
        }

        override fun onFinish() {
            timerIsRunning = false
            onFinish?.invoke()
        }
    }

    private var timerIsRunning = false

    private var onTick: ((Long) -> Unit)? = null
    private var onFinish: (() -> Unit)? = null

    fun setOnTimerTick(listener: (Long) -> Unit) {
        onTick = listener
    }

    fun setOnTimerFinish(listener: () -> Unit) {
        onFinish = listener
    }

    fun sendAuthCode() = invokeDisposable {
        authUseCase.sendAuthCode()
            .doOnSubscribe { _state.postValue(AuthState.Loading) }
            .subscribe({
                _state.postValue(AuthState.CodeSent)
            }, {
                when (it) {
                    is InternalServerError -> _effect.postValue(AuthEffect.NetworkError)
                    else -> _effect.postValue(AuthEffect.Error)
                }
                _state.postValue(AuthState.Idle)
            })
    }

    fun signUp() = invokeDisposable {
        authUseCase.signUp(user)
            .doOnSubscribe { _state.postValue(AuthState.Loading) }
            .subscribe({
                _state.postValue(AuthState.Success)
            }, {
                when (it) {
                    is InternalServerError -> _effect.postValue(AuthEffect.NetworkError)
                    is EmailAlreadyRegistered -> _effect.postValue(AuthEffect.EmailAlreadyRegistered)
                    else -> _effect.postValue(AuthEffect.Error)
                }
                _state.postValue(AuthState.Idle)
            })
    }

    fun signIn() = invokeDisposable {
        authUseCase.signIn(user)
            .doOnSubscribe {
                _state.postValue(AuthState.Loading) }
            .subscribe({
                _state.postValue(AuthState.Success)
            }, {
                when (it) {
                    is InternalServerError -> _effect.postValue(AuthEffect.NetworkError)
                    is IncorrectPassword -> _effect.postValue(AuthEffect.IncorrectPassword)
                    else -> _effect.postValue(AuthEffect.Error)
                }
                _state.postValue(AuthState.Idle)
            })
    }

    fun confirmAuthCode(code: String) = invokeDisposable {
        authUseCase.confirmAuthCode(code)
            .doOnSubscribe { _state.postValue(AuthState.Loading) }
            .subscribe({
                _state.postValue(AuthState.Success)
            }, {
                when (it) {
                    is InternalServerError -> _effect.postValue(AuthEffect.NetworkError)
                    is IncorrectCode -> _effect.postValue(AuthEffect.IncorrectCode)
                    else -> _effect.postValue(AuthEffect.Error)
                }
                _state.postValue(AuthState.Idle)
            })
    }

    fun startTimer() {
        if (timerIsRunning) return

        countDownTimer.start()
    }

    fun stopTimer() {
        if (!timerIsRunning) return

        countDownTimer.cancel()
    }

    sealed class AuthState : State {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data object Success : AuthState()
        data object CodeSent : AuthState()
    }

    sealed class AuthEffect : Effect {
        data object NetworkError : AuthEffect()
        data object EmailAlreadyRegistered : AuthEffect()
        data object EmailNotRegistered : AuthEffect()
        data object IncorrectPassword : AuthEffect()
        data object IncorrectCode : AuthEffect()
        data object Error : AuthEffect()
    }
}