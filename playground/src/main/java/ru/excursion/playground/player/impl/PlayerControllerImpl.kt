package ru.excursion.playground.player.impl

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.excursion.playground.player.PlayerController


class PlayerControllerImpl(mMediaPlayer: MediaPlayer, private val stopSelf: () -> Unit) :
    PlayerController {

    private var currentPosition: Int = 0

    private val duration = MutableLiveData<Int>()
    private val isPlaying = MutableLiveData<Boolean>()

    private val mediaPlayer = mMediaPlayer.apply {
        setOnPreparedListener(::onPrepared)
        setOnErrorListener(::onError)
        setOnCompletionListener(::onCompletion)
        isLooping = true
    }

    override fun playMedia() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(currentPosition)
            mediaPlayer.start()
            isPlaying.postValue(mediaPlayer.isPlaying)
        }
    }

    override fun stopMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            isPlaying.postValue(mediaPlayer.isPlaying)
        }
    }

    override fun pauseMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            currentPosition = mediaPlayer.currentPosition
            isPlaying.postValue(mediaPlayer.isPlaying)
        }
    }

    override fun resumeMedia() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(currentPosition)
            mediaPlayer.start()
            isPlaying.postValue(mediaPlayer.isPlaying)
        }
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) =
        mediaPlayer.setVolume(leftVolume, rightVolume)

    override fun release() = mediaPlayer.release()

    private fun onPrepared(mp: MediaPlayer?) {
        duration.postValue(mp?.duration)
    }

    private fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
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

    private fun onCompletion(mp: MediaPlayer?) {
        pauseMedia()
    }

    override fun setCurrentPosition(position: Int) {
        currentPosition = position
    }

    override fun observeDuration(lifecycleOwner: LifecycleOwner, callback: (Int) -> Unit) =
        duration.observe(lifecycleOwner, callback)

    override fun observeIsPlaying(lifecycleOwner: LifecycleOwner, callback: (Boolean) -> Unit) =
        isPlaying.observe(lifecycleOwner, callback)
}