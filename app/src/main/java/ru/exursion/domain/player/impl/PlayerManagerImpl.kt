package ru.exursion.domain.player.impl

import android.content.Context
import android.content.Intent
import ru.exursion.domain.player.ACTION_PAUSE
import ru.exursion.domain.player.ACTION_PLAY
import ru.exursion.domain.player.ACTION_SET_MEDIA
import ru.exursion.domain.player.ACTION_SET_POSITION
import ru.exursion.domain.player.MediaPlayerService
import ru.exursion.domain.player.interfaces.MediaPlayerController
import ru.exursion.domain.player.interfaces.PlayerManager

class PlayerManagerImpl(private val context: Context): PlayerManager {

    override fun play() = callService(ACTION_PLAY)

    override fun pause() = callService(ACTION_PAUSE)

    override fun setMedia(mediaLink: String) = callService(ACTION_SET_MEDIA, "mediaLink", mediaLink)

    override fun setPosition(position: Int) = callService(ACTION_SET_POSITION, "position", position)

    override fun getIsPlayingObservable() = MediaPlayerController.observeIsPlaying()

    override fun getCurrentPositionObservable() = MediaPlayerController.observeCurrentPosition()

    private fun callService(act: String) {
        context.startService(
            Intent(
                context,
                MediaPlayerService::class.java
            ).apply {
                action = act
            })
    }

    private fun callService(act: String, name: String, value: String) {
        context.startService(
            Intent(
                context,
                MediaPlayerService::class.java
            ).apply {
                action = act
                putExtra(name, value)
            })
    }

    private fun callService(act: String, name: String, value: Int) {
        context.startService(
            Intent(
                context,
                MediaPlayerService::class.java
            ).apply {
                action = act
                putExtra(name, value)
            })
    }

}