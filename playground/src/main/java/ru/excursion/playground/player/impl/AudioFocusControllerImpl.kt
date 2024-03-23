package ru.excursion.playground.player.impl

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import ru.excursion.playground.player.AudioFocusController
import ru.excursion.playground.player.PlayerController


class AudioFocusControllerImpl(
    private var playerController: PlayerController?,
    context: Context
) : AudioFocusController {

    private var audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        )
        .setOnAudioFocusChangeListener(this)
        .setAcceptsDelayedFocusGain(true)
        .build()

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                playerController?.playMedia()
                playerController?.setVolume(1.0f, 1.0f)
            }

            AudioManager.AUDIOFOCUS_LOSS -> {
                playerController?.stopMedia()
                playerController?.release()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ->
                playerController?.pauseMedia()

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                playerController?.setVolume(1.0f, 1.0f)
        }
    }

    override fun requestAudioFocus(): Boolean =
        audioManager.requestAudioFocus(audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED


    override fun removeAudioFocus() =
        audioManager.abandonAudioFocusRequest(audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
}