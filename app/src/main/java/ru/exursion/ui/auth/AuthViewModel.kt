package ru.exursion.ui.auth

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import javax.inject.Inject

const val TIMER_FINISH_TIME = 30000L
const val TIMER_STEP_INTERVAL = 1000L

class AuthViewModel @Inject constructor(): ViewModel() {

    private val countDownTimer: CountDownTimer = object : CountDownTimer(TIMER_FINISH_TIME, TIMER_STEP_INTERVAL) {
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

    fun sendMessageToEmail(email: String) {

    }

    fun checkCode(code: String) {

    }

    fun setOnTimerTick(listener: (Long) -> Unit) { onTick = listener }

    fun setOnTimerFinish(listener: () -> Unit) { onFinish = listener }

    fun startTimer() {
        if (timerIsRunning) return
        countDownTimer.start()
    }
}