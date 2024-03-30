package ru.excursion.playground.player.impl

import android.media.AudioFocusRequest
import android.media.AudioManager
import ru.excursion.playground.player.interfaces.AudioFocusCallbacks
import ru.excursion.playground.player.interfaces.AudioFocusController
import ru.excursion.playground.player.interfaces.MediaPlayerController


class AudioFocusControllerImpl(
    audioFocusRequestBuilder: AudioFocusRequest.Builder,
    mediaPlayerController: MediaPlayerController,
    private val audioManager: AudioManager
) : AudioFocusController {

    private val audioFocusCallbacks: AudioFocusCallbacks = AudioFocusCallbacksImpl(mediaPlayerController)

    private val audioFocusRequest = audioFocusRequestBuilder
        .setOnAudioFocusChangeListener(audioFocusCallbacks::onAudioFocusChange)
        .build()

    override fun requestAudioFocus(): Boolean =
        audioManager.requestAudioFocus(audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED


    override fun removeAudioFocus() =
        audioManager.abandonAudioFocusRequest(audioFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
}