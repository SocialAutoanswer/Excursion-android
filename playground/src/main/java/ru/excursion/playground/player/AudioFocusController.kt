package ru.excursion.playground.player

import android.media.AudioManager

interface AudioFocusController: AudioManager.OnAudioFocusChangeListener {
    override fun onAudioFocusChange(focusChange: Int)

    fun requestAudioFocus(): Boolean

    fun removeAudioFocus(): Boolean
}