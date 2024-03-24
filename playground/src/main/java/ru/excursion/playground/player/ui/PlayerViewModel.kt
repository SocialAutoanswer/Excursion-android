package ru.excursion.playground.player.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.excursion.playground.player.MediaPlayerService

class PlayerViewModel: ViewModel() {

    var player: MediaPlayerService? = null
    var serviceBound = false
    var duration = 0
    val timerHandler: Handler = Handler(Looper.getMainLooper())

}