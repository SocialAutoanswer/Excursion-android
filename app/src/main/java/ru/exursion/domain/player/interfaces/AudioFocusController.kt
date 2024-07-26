package ru.exursion.domain.player.interfaces


interface AudioFocusController {
    fun requestAudioFocus(): Boolean

    fun removeAudioFocus(): Boolean
}