package ru.excursion.playground.player

import android.media.MediaPlayer

interface PlayerController : MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {

    fun initPlayer(mediaLink: String): MediaPlayer

    fun playMedia()

    fun stopMedia()

    fun pauseMedia()

    fun resumeMedia()

    fun setVolume(leftVolume: Float, rightVolume: Float)

    fun release()

    fun getDuration(): Int

    override fun onPrepared(mp: MediaPlayer?)

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean

    override fun onCompletion(mp: MediaPlayer?)
}