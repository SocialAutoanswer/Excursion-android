package ru.excursion.playground.player

import android.media.AudioManager

interface AudioFocusController {
    fun requestAudioFocus(): Boolean

    fun removeAudioFocus(): Boolean
}