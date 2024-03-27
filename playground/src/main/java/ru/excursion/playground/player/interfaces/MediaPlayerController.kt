package ru.excursion.playground.player.interfaces

interface MediaPlayerController {

    var currentPosition: Int

    fun playMedia()

    fun stopMedia()

    fun pauseMedia()

    fun setVolume(leftVolume: Float, rightVolume: Float)

    fun release()

    fun setMedia(mediaLink: String)

}