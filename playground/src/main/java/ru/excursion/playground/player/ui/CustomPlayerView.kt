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
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import ru.excursion.playground.R
import ru.excursion.playground.databinding.AudioPlayerBinding
import ru.excursion.playground.player.ACTION_SET_POSITION
import ru.excursion.playground.player.MediaPlayerService
import ru.excursion.playground.player.PlayerServiceController
import ru.excursion.playground.toTimeFormat

sealed class PlayerUI(
    @DrawableRes
    val img: Int,
    val onClick: () -> Unit
) {
    data object Plays : PlayerUI(R.drawable.ic_pause, PlayerServiceController::pause)
    data object Paused : PlayerUI(R.drawable.img, PlayerServiceController::play)
}

class CustomPlayerView(context: Context, attributes: AttributeSet) : CardView(context, attributes) {

    private val binding = AudioPlayerBinding.inflate(LayoutInflater.from(context), this, false)
    private var duration = 0
    private val timerHandler: Handler = Handler(Looper.getMainLooper())
    var name = ""

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
                PlayerServiceController.pause()
            }
        }
    }

    private val onSeekBarChangeListener = object : SimpleOnSeekBarChangedListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            binding.currentTime.text = (seekBar?.progress ?: 0).toTimeFormat()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            PlayerServiceController.pause()
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            PlayerServiceController.setPosition((seekBar?.progress ?: 0) * 1000)
            binding.currentTime.text = (seekBar?.progress ?: 0).toTimeFormat()
            PlayerServiceController.play()
        }
    }


    init {
        prepareObservers()
        setPlayerUI(PlayerUI.Paused)
        binding.trackBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
        addView(binding.root)
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
            putParcelable("superState", super.onSaveInstanceState())
            putString("name", name)
        }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var mState = state

        if (state is Bundle) {
            name = state.getString("name") ?: ""
            mState = state.getParcelable("superState")
        }
        super.onRestoreInstanceState(mState)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        binding.name.text = name
    }


    private fun setPlayerUI(playerUI: PlayerUI) {
        with(binding) {
            playBtn.setImageResource(playerUI.img)
            playBtn.setOnClickListener { playerUI.onClick.invoke() }
        }
    }



    @SuppressLint("CheckResult")
    private fun prepareObservers() {
        MediaPlayerService.observeDuration().subscribe { dur ->
            duration = dur / 1000
            binding.trackDuration.text = duration.toTimeFormat()
            binding.trackBar.max = duration
        }

        MediaPlayerService.observeIsPlaying().subscribe { isPlaying ->
            if (isPlaying) {
                setPlayerUI(PlayerUI.Plays)
                timerHandler.post(timerRunnable)
            } else {
                setPlayerUI(PlayerUI.Paused)
                timerHandler.removeCallbacks(timerRunnable)
            }
        }

    }

}