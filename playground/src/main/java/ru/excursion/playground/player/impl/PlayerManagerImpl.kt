package ru.excursion.playground.player.impl

import android.content.Context
import android.content.Intent
import ru.excursion.playground.player.ACTION_PAUSE
import ru.excursion.playground.player.ACTION_PLAY
import ru.excursion.playground.player.ACTION_SET_MEDIA
import ru.excursion.playground.player.ACTION_SET_POSITION
import ru.excursion.playground.player.MediaPlayerService
import ru.excursion.playground.player.interfaces.MediaPlayerController
import ru.excursion.playground.player.interfaces.OnPlayerClickListener
import ru.excursion.playground.player.interfaces.PlayerManager
import javax.inject.Singleton

@Singleton
class PlayerManagerImpl(private val context: Context): PlayerManager {

    override fun play() = callService(ACTION_PLAY)

    override fun pause() = callService(ACTION_PAUSE)

    override fun setMedia(mediaLink: String) = callService(ACTION_SET_MEDIA, "mediaLink", mediaLink)

    override fun setPosition(position: Int) = callService(ACTION_SET_POSITION, "position", position)

    override fun getDurationObservable() = MediaPlayerController.observeDuration()
    override fun getIsPlayingObservable() = MediaPlayerController.observeIsPlaying()

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