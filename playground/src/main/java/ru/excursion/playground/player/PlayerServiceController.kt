package ru.excursion.playground.player

import android.content.Intent
import ru.excursion.playground.player.App.Companion.context

object PlayerServiceController {

    fun play() = callService(ACTION_PLAY)

    fun pause() = callService(ACTION_PAUSE)

    fun setMedia(mediaLink: String) = callService(ACTION_SET_MEDIA, "mediaLink", mediaLink)

    fun setPosition(position: Int) = callService(ACTION_SET_POSITION, "position", position)

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