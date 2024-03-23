package ru.excursion.playground.player.ui

import android.widget.SeekBar

interface SimpleOnSeekBarChangedListener: SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?)
}