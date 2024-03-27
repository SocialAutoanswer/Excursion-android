package ru.excursion.playground.player.impl

import android.media.AudioFocusRequest
import android.media.AudioManager
import ru.excursion.playground.player.interfaces.AudioFocusController
import ru.excursion.playground.player.interfaces.MediaPlayerController


class AudioFocusControllerImpl(
    audioFocusRequestBuilder: AudioFocusRequest.Builder,
    private val mediaPlayerController: MediaPlayerController,
    private val audioManager: AudioManager
) : AudioFocusController {

    private val audioFocusRequest = audioFocusRequestBuilder
        .setOnAudioFocusChangeListener(::onAudioFocusChange)
        .build()

    private fun onAudioFocusChange(focusChange: Int) {
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

    override fun requestAudioFocus(): Boolean =
        audioManager.requestAudioFocus(audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED


    override fun removeAudioFocus() =
        audioManager.abandonAudioFocusRequest(audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
}