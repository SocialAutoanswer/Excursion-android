package ru.exursion.ui.auth.vm

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import ru.exursion.domain.AuthUseCase
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {

    companion object {
        private const val TIMER_FINISH_TIME = 30000L
        private const val TIMER_STEP_INTERVAL = 1000L
    }

    var email: String? = null

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

    fun setOnTimerTick(listener: (Long) -> Unit) { onTick = listener }

    fun setOnTimerFinish(listener: () -> Unit) { onFinish = listener }

    fun sendAuthCode() = email?.let {
        authUseCase.sendAuthCode(it).subscribe()
    } //TODO:make states for loading and add to ComponentDisposable

    fun confirmAuthCode(code: String) = authUseCase.confirmAuthCode(code).subscribe() //TODO:make states for loading and add to ComponentDisposable

    fun signUp() = authUseCase.signUp().subscribe() //TODO:make states for loading and add to ComponentDisposable

    fun signIn() = authUseCase.signIn().subscribe() //TODO:make states for loading and add to ComponentDisposable


    fun startTimer() {
        if (timerIsRunning) return

        countDownTimer.start()
    }

    fun stopTimer() {
        if (!timerIsRunning) return

        countDownTimer.cancel()
    }
}