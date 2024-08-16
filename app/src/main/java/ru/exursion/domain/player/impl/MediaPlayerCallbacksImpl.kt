package ru.exursion.domain.player.impl

import android.media.MediaPlayer
import android.util.Log
import ru.bibaboba.kit.Logger
import ru.exursion.domain.player.interfaces.MediaPlayerCallbacks
import ru.exursion.domain.player.interfaces.MediaPlayerController

class MediaPlayerCallbacksImpl(
    private val mediaPlayerController: MediaPlayerController,
) : MediaPlayerCallbacks {

    override fun onPrepared(mp: MediaPlayer?) {}

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        when (what) {
            MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK $extra"
            )

            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR SERVER DIED $extra"
            )

            MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                Logger.debug(this::class, "MEDIA ERROR UNKNOWN $extra")
                mediaPlayerController.stopMedia()
                return true
            }
        }
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayerController.setPosition(0)
        mediaPlayerController.pauseMedia()
    }
}