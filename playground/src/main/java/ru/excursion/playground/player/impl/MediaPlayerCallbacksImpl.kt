package ru.excursion.playground.player.impl

import android.media.MediaPlayer
import android.util.Log
import ru.excursion.playground.player.interfaces.MediaPlayerCallbacks
import ru.excursion.playground.player.interfaces.MediaPlayerController

class MediaPlayerCallbacksImpl(
    private val mediaPlayerController: MediaPlayerController,
    private val updateDuration: (Int) -> Unit
) : MediaPlayerCallbacks {

    override fun onPrepared(mp: MediaPlayer?) {
        if (mp != null) {
            updateDuration(mp.duration)
        }
    }

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

            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR UNKNOWN $extra"
            )
        }
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayerController.currentPosition = 0
        mediaPlayerController.pauseMedia()
    }
}