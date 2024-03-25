package ru.excursion.playground.player

import android.media.MediaPlayer
import androidx.lifecycle.LifecycleOwner

interface PlayerController {

    fun playMedia()

    fun stopMedia()

    fun pauseMedia()

    fun resumeMedia()

    fun setVolume(leftVolume: Float, rightVolume: Float)

    fun release()

    fun setCurrentPosition(position: Int)

    fun observeDuration(lifecycleOwner: LifecycleOwner, callback: (Int) -> Unit)

    fun observeIsPlaying(lifecycleOwner: LifecycleOwner, callback: (Boolean) -> Unit)

}