package ru.excursion.playground.player.impl

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.excursion.playground.player.PlayerController


class PlayerControllerImpl(mediaLink: String, private val stopSelf: () -> Unit) : PlayerController {

    private val mediaPlayer = initPlayer(mediaLink)
    private var currentPosition: Int = 0

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> = _duration

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPLaying: LiveData<Boolean> = _isPlaying

    override fun playMedia() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(currentPosition)
            mediaPlayer.start()
            _isPlaying.postValue(mediaPlayer.isPlaying)
        }
    }

    override fun stopMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            _isPlaying.postValue(mediaPlayer.isPlaying)
        }
    }

    override fun pauseMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            currentPosition = mediaPlayer.currentPosition
            _isPlaying.postValue(mediaPlayer.isPlaying)
        }
    }

    override fun resumeMedia() {
        if (!mediaPlayer.isPlaying) {
            //Log.d("myfuckingtag", "before $currentPosition local \n ${mediaPlayer.currentPosition} mp \n\n")
            mediaPlayer.seekTo(currentPosition)
            //Log.d("myfuckingtag", "after $currentPosition local \n ${mediaPlayer.currentPosition} mp \n\n")
            mediaPlayer.start()
            _isPlaying.postValue(mediaPlayer.isPlaying)
        }
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) =
        mediaPlayer.setVolume(leftVolume, rightVolume)

    override fun release() = mediaPlayer.release()

    override fun onPrepared(mp: MediaPlayer?) {
        _duration.postValue(mp?.duration)
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
        stopMedia()
        stopSelf.invoke()
    }

    override fun initPlayer(mediaLink: String): MediaPlayer = MediaPlayer().apply {

        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        setOnPreparedListener(this@PlayerControllerImpl)
        setOnErrorListener(this@PlayerControllerImpl)
        setOnCompletionListener(this@PlayerControllerImpl)

        reset()
        setDataSource(mediaLink)
        prepareAsync()
    }

    override fun setCurrentPosition(position: Int) {
        currentPosition = position

        Log.d("myfuckingtag", "current position: $currentPosition")
        Log.d("myfuckingtag", (Looper.getMainLooper().thread == Thread.currentThread()).toString())
    }

}