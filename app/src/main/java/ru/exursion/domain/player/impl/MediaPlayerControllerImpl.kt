package ru.exursion.domain.player.impl

import android.media.MediaPlayer
import ru.exursion.domain.player.interfaces.MediaPlayerCallbacks
import ru.exursion.domain.player.interfaces.MediaPlayerController
import java.util.Timer
import java.util.TimerTask


class MediaPlayerControllerImpl(
    _mediaPlayer: MediaPlayer
) : MediaPlayerController {

    private val mediaPlayerCallbacks: MediaPlayerCallbacks = MediaPlayerCallbacksImpl(this)

    private val mediaPlayer = _mediaPlayer.apply {
        setOnPreparedListener(mediaPlayerCallbacks::onPrepared)
        setOnErrorListener(mediaPlayerCallbacks::onError)
        setOnCompletionListener(mediaPlayerCallbacks::onCompletion)
    }

    private var currentDataSource: String? = null

    private var timer: Timer? = null

    private fun startTimer() {
        timer = Timer()

        timer?.schedule(object : TimerTask() {
            override fun run() {
                updateCurrentPosition(mediaPlayer.currentPosition)
            }
        }, 0, 1000)
    }

    private fun stopTimer() = timer?.cancel()

    override fun playMedia() {
        if (!mediaPlayer.isPlaying) {
            startTimer()
            mediaPlayer.start()
            updateIsPlaying(mediaPlayer.isPlaying)
        }
    }

    override fun stopMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            stopTimer()
            updateIsPlaying(mediaPlayer.isPlaying)
        }
    }

    override fun pauseMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            stopTimer()
            updateIsPlaying(mediaPlayer.isPlaying)
        }
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) =
        mediaPlayer.setVolume(leftVolume, rightVolume)

    override fun setPosition(position: Int) {
        mediaPlayer.seekTo(position.toLong(), MediaPlayer.SEEK_CLOSEST)
        updateCurrentPosition(position)
    }

    override fun release() {
        if (mediaPlayer.isPlaying) {
            stopTimer()
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    override fun setMedia(mediaLink: String) {
        if (currentDataSource == mediaLink) {
            playMedia()
            return
        }
        currentDataSource = mediaLink
        mediaPlayer.reset()
        mediaPlayer.setDataSource(mediaLink)
        mediaPlayer.prepare()
        playMedia()
    }

}