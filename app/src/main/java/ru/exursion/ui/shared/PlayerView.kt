package ru.exursion.ui.shared

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import ru.bibaboba.kit.ui.utils.SimpleOnSeekBarChangedListener
import ru.exursion.R
import ru.exursion.databinding.LayoutPlayerBinding
import ru.exursion.ui.shared.ext.toTimeFormat


class PlayerView(context: Context, attributes: AttributeSet) : ConstraintLayout(context, attributes) {

    private var trackName = ""

    private val binding = LayoutPlayerBinding.inflate(LayoutInflater.from(context), this, true)
    private var duration = 0
    private var _playerClickListener : OnPlayerClickListener? = null

    private var playerClickListener = object : OnPlayerClickListener {
        override fun onPlayClick() {
            setPlayerUI(true)
            _playerClickListener?.onPlayClick()
        }

        override fun onPauseClick() {
            setPlayerUI(false)
            _playerClickListener?.onPauseClick()
        }

        override fun onSetPosition(position: Int) {
            setCurrentPosition(position)
            _playerClickListener?.onSetPosition(position)
        }
    }

    private val onSeekBarChangeListener = object : SimpleOnSeekBarChangedListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            binding.currentTime.text = (seekBar?.progress ?: 0).toTimeFormat()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            playerClickListener.onPauseClick()
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            playerClickListener.onSetPosition((seekBar?.progress ?: 0) * 1000)
            binding.currentTime.text = (seekBar?.progress ?: 0).toTimeFormat()
            playerClickListener.onPlayClick()
        }
    }


    init {
        binding.trackBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putString("name", trackName)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var _state = state

        if (state is Bundle) {
            trackName = state.getString("name") ?: ""
            _state = state.getParcelable("superState")
        }
        super.onRestoreInstanceState(_state)
    }


    fun setOnPlayerClickListener(listener: OnPlayerClickListener) {
        _playerClickListener = listener
    }

    fun setCurrentPosition(currentPosition: Int) {
        with(binding) {
            trackBar.progress = currentPosition / 1000
            currentTime.text = (currentPosition / 1000).toTimeFormat()
        }

//        if (binding.trackBar.progress == duration) {
//            binding.trackBar.progress = 0
//            binding.currentTime.text = 0.toTimeFormat()
//            playerClickListener.onPauseClick()
//        }
    }

    fun setDuration(dur: Int) {
        duration = dur / 1000 //durations is given in milliseconds (divide by 1000 gives seconds)
        binding.trackDuration.text = duration.toTimeFormat()
        binding.trackBar.max = duration
    }

    fun setTrackName(trackName: String) {
        this.trackName = trackName
        binding.trackName.text = trackName
    }


    fun setPlayerUI(isPlaying: Boolean) {
        with(binding) {
            if(isPlaying){
                playBtn.setImageResource(R.drawable.ic_pause)
                playBtn.setOnClickListener { playerClickListener.onPauseClick() }
            } else {
                playBtn.setImageResource(R.drawable.ic_play)
                playBtn.setOnClickListener { playerClickListener.onPlayClick() }
            }
        }
    }


}