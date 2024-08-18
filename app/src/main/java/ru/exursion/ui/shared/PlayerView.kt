package ru.exursion.ui.shared

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import ru.bibaboba.kit.ui.utils.SimpleOnSeekBarChangedListener
import ru.exursion.databinding.LayoutPlayerBinding
import ru.exursion.ui.shared.ext.toTimeFormat


class PlayerView(context: Context, attributes: AttributeSet) : ConstraintLayout(context, attributes) {

    private var trackName = ""

    private val binding = LayoutPlayerBinding.inflate(LayoutInflater.from(context), this, true)
    private var duration = 0
    private var playerClickListener : OnPlayerClickListener? = null
    val playButton: PlayButton = binding.playBtn

    private val onSeekBarChangeListener = object : SimpleOnSeekBarChangedListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            binding.currentTime.text = (seekBar?.progress ?: 0).toTimeFormat()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            playerClickListener?.onPauseClick()
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            playerClickListener?.onSetPosition((seekBar?.progress ?: 0) * 1000)
            binding.currentTime.text = (seekBar?.progress ?: 0).toTimeFormat()
            playerClickListener?.onPlayClick()
        }
    }


    init {
        binding.trackName.text = trackName
        binding.trackDuration.text = duration.toTimeFormat()
        binding.trackBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
        playerClickListener?.let {
            binding.playBtn.setOnPlayerClickListener(it)
        }
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putString("name", trackName)
        putInt("duration", duration)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var _state = state

        if (state is Bundle) {
            trackName = state.getString("name") ?: ""
            duration = state.getInt("duration")
            _state = state.getParcelable("superState")
        }
        super.onRestoreInstanceState(_state)
    }


    fun setOnPlayerClickListener(listener: OnPlayerClickListener) {
        playerClickListener = listener
        binding.playBtn.setOnPlayerClickListener(listener)
    }

    fun setCurrentPosition(currentPosition: Int) {
        with(binding) {
            trackBar.progress = currentPosition / 1000
            currentTime.text = (currentPosition / 1000).toTimeFormat()
        }
    }

    fun setDurationInSeconds(dur: Int) {
        binding.trackDuration.text = dur.toTimeFormat()
        binding.trackBar.max = dur
    }

    fun setTrackName(trackName: String) {
        this.trackName = trackName
        binding.trackName.text = trackName
    }

}