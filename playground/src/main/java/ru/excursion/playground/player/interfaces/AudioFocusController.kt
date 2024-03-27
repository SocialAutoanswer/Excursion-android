package ru.excursion.playground.player.interfaces

import android.media.AudioManager

interface AudioFocusController {
    fun requestAudioFocus(): Boolean

    fun removeAudioFocus(): Boolean
}