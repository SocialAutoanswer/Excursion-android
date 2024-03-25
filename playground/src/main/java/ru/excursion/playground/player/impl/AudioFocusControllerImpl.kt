package ru.excursion.playground.player.impl

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import ru.excursion.playground.player.AudioFocusController
import ru.excursion.playground.player.PlayerController
import javax.inject.Inject


class AudioFocusControllerImpl(
    audioFocusRequestBuilder: AudioFocusRequest.Builder,
    private val playerController: PlayerController,
    private val audioManager: AudioManager
) : AudioFocusController {

    private val audioFocusRequest = audioFocusRequestBuilder
        .setOnAudioFocusChangeListener(::onAudioFocusChange)
        .build()

    private fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                playerController.playMedia()
                playerController.setVolume(1.0f, 1.0f)
            }

            AudioManager.AUDIOFOCUS_LOSS ->
                playerController.pauseMedia()


            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ->
                playerController.pauseMedia()

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                playerController.setVolume(1.0f, 1.0f)
        }
    }

    override fun requestAudioFocus(): Boolean =
        audioManager.requestAudioFocus(audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED


    override fun removeAudioFocus() =
        audioManager.abandonAudioFocusRequest(audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
}