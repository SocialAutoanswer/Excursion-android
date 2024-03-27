package ru.excursion.playground.player.impl

import android.content.Context
import android.media.MediaPlayer
import ru.excursion.playground.player.MediaPlayerService
import ru.excursion.playground.player.interfaces.MediaPlayerCallbacks
import ru.excursion.playground.player.interfaces.MediaPlayerController


class MediaPlayerControllerImpl(
    mMediaPlayer: MediaPlayer,
    updateDuration: (Int) -> Unit,
    private val updateIsPlaying: (Boolean) -> Unit
) : MediaPlayerController {

    private val mediaPlayerCallbacks: MediaPlayerCallbacks = MediaPlayerCallbacksImpl(this, updateDuration)
    private var isDataSourceAdded = false

    private val mediaPlayer = mMediaPlayer.apply {
        setOnPreparedListener(mediaPlayerCallbacks::onPrepared)
        setOnErrorListener(mediaPlayerCallbacks::onError)
        setOnCompletionListener(mediaPlayerCallbacks::onCompletion)
        isLooping = true
    }

    override var currentPosition: Int = 0
    override fun playMedia() {
        if (!mediaPlayer.isPlaying) {
            if(currentPosition != 0) {
                mediaPlayer.seekTo(currentPosition)
            }
            mediaPlayer.start()
            updateIsPlaying(mediaPlayer.isPlaying)
        }
    }

    override fun stopMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            updateIsPlaying(mediaPlayer.isPlaying)
        }
    }

    override fun pauseMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            currentPosition = mediaPlayer.currentPosition
            updateIsPlaying(mediaPlayer.isPlaying)
        }
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) =
        mediaPlayer.setVolume(leftVolume, rightVolume)

    override fun release() {
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        isDataSourceAdded = false
    }

    override fun setMedia(mediaLink: String) {
        if(!isDataSourceAdded) {
            mediaPlayer.setDataSource(mediaLink)
            isDataSourceAdded = true
            mediaPlayer.prepareAsync()
        }
    }

}