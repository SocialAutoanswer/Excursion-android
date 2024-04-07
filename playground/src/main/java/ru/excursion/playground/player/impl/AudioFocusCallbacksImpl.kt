package ru.excursion.playground.player.impl

import android.media.AudioManager
import ru.excursion.playground.player.interfaces.AudioFocusCallbacks
import ru.excursion.playground.player.interfaces.MediaPlayerController

class AudioFocusCallbacksImpl(private val mediaPlayerController: MediaPlayerController): AudioFocusCallbacks {

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                mediaPlayerController.playMedia()
                mediaPlayerController.setVolume(1.0f, 1.0f)
            }

            AudioManager.AUDIOFOCUS_LOSS ->
                mediaPlayerController.pauseMedia()


            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ->
                mediaPlayerController.pauseMedia()

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                mediaPlayerController.setVolume(1.0f, 1.0f)
        }
    }
}