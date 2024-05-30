package ru.exursion.ui.auth.vm

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class AuthViewModel @Inject constructor(): ViewModel() {

    companion object {
        private const val TIMER_FINISH_TIME = 30000L
        private const val TIMER_STEP_INTERVAL = 1000L
    }

    var email: String? = null
    var password: String? = null

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

    fun tryToLoginOrSendCodeToEmail() {

    }

    fun startTimer() {
        if (timerIsRunning) return

        countDownTimer.start()
    }

    fun stopTimer() {
        if (!timerIsRunning) return

        countDownTimer.cancel()
    }
}