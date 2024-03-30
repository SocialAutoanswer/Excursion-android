package ru.excursion.playground.player.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.cardview.widget.CardView
import io.reactivex.rxjava3.core.Observable
import ru.excursion.playground.R
import ru.excursion.playground.databinding.AudioPlayerBinding
import ru.excursion.playground.player.MediaPlayerService
import ru.excursion.playground.player.interfaces.MediaPlayerController
import ru.excursion.playground.player.interfaces.OnPlayerClickListener
import ru.excursion.playground.toTimeFormat


class PlayerView(context: Context, attributes: AttributeSet) : CardView(context, attributes) {

    var placeName = ""

    private val binding = AudioPlayerBinding.inflate(LayoutInflater.from(context), this, false)
    private var duration = 0
    private val timerHandler: Handler = Handler(Looper.getMainLooper())
    private var playerClickListener : OnPlayerClickListener? = null

    private var timerRunnable = object : Runnable {

        override fun run() {
            with(binding) {
                trackBar.progress += 1
                currentTime.text = trackBar.progress.toTimeFormat()
            }

            timerHandler.postDelayed(this, 1000)

            if (binding.trackBar.progress == duration) {
                binding.trackBar.progress = 0
                binding.currentTime.text = 0.toTimeFormat()
                playerClickListener?.onPauseClick()
            }
        }
    }

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
        setPlayerUI(false)
        binding.trackBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
        addView(binding.root)
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putString("name", placeName)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var _state = state

        if (state is Bundle) {
            placeName = state.getString("name") ?: ""
            _state = state.getParcelable("superState")
        }
        super.onRestoreInstanceState(_state)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        binding.name.text = placeName
    }

    fun setOnPlayerClickListener(listener: OnPlayerClickListener) {
        playerClickListener = listener
    }

    @SuppressLint("CheckResult")
    fun setDurationObservable(durationObservable: Observable<Int>) {
        durationObservable.subscribe { dur ->
            duration = dur / 1000 //durations is given in milliseconds (divide by 1000 gives seconds)
            binding.trackDuration.text = duration.toTimeFormat()
            binding.trackBar.max = duration
        }
    }

    @SuppressLint("CheckResult")
    fun setIsPlayingObservable(isPlayingObservable: Observable<Boolean>) {
        isPlayingObservable.subscribe { isPlaying ->
            setPlayerUI(isPlaying)

            if (isPlaying) {
                timerHandler.post(timerRunnable)
            } else {
                timerHandler.removeCallbacks(timerRunnable)
            }
        }
    }


    private fun setPlayerUI(isPlaying: Boolean) {
        with(binding) {
            if(isPlaying){
                playBtn.setImageResource(R.drawable.ic_pause)
                playBtn.setOnClickListener { playerClickListener?.onPauseClick() }
            } else {
                playBtn.setImageResource(R.drawable.ic_play)
                playBtn.setOnClickListener { playerClickListener?.onPlayClick() }
            }
        }
    }


}