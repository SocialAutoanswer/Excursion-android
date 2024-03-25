package ru.excursion.playground.player.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import ru.excursion.playground.R
import ru.excursion.playground.databinding.AudioPlayerBinding
import ru.excursion.playground.player.ACTION_PAUSE
import ru.excursion.playground.player.ACTION_PLAY
import ru.excursion.playground.player.ACTION_RESUME
import ru.excursion.playground.player.ACTION_SET_POSITION
import ru.excursion.playground.player.MediaPlayerService
import ru.excursion.playground.toTimeFormat

sealed class PlayerUI(
    @DrawableRes
    val img: Int,
    val action: String
) {
    data object Plays : PlayerUI(R.drawable.ic_pause, ACTION_PAUSE)
    data object Paused : PlayerUI(R.drawable.img, ACTION_PLAY)
}

class CustomPlayerView(context: Context, attributes: AttributeSet) : CardView(context, attributes) {

    private val binding = AudioPlayerBinding.inflate(LayoutInflater.from(context), this, false)
    private var serviceBound = false
    private var duration = 0
    private val timerHandler: Handler = Handler(Looper.getMainLooper())
    private var player: MediaPlayerService? = null
    var mediaLink = ""

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
                callService(ACTION_PAUSE)
            }
        }
    }

    private val onSeekBarChangeListener = object : SimpleOnSeekBarChangedListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            binding.currentTime.text = seekBar!!.progress.toTimeFormat()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            callService(ACTION_PAUSE)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            callService(ACTION_SET_POSITION, "position", seekBar!!.progress * 1000)
            binding.currentTime.text = seekBar.progress.toTimeFormat()
            callService(ACTION_RESUME)
        }
    }


    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MediaPlayerService.LocalBinder
            player = binder.service
            serviceBound = true
            prepareObservers()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }


    init {
        addView(binding.root)

        setPlayerUI(PlayerUI.Paused)
        binding.trackBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
    }


    private fun setPlayerUI(playerUI: PlayerUI) {
        with(binding) {
            playBtn.setImageResource(playerUI.img)
            playBtn.setOnClickListener { callService(playerUI.action) }
        }
    }


    private fun callService(act: String) {
        if (serviceBound) {
            context.startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).apply {
                    action = act
                })
        } else {
            Toast.makeText(context, R.string.player_unable, Toast.LENGTH_SHORT).show()
        }
    }


    private fun callService(act: String, name: String, value: Int) {
        if (serviceBound) {
            context.startService(
                Intent(
                    context,
                    MediaPlayerService::class.java
                ).apply {
                    action = act
                    putExtra(name, value)
                })
        } else {
            Toast.makeText(context, R.string.player_unable, Toast.LENGTH_SHORT).show()
        }
    }


    private fun prepareObservers() {
        player!!.observeDuration { dur ->
            duration = dur / 1000
            binding.trackDuration.text = duration.toTimeFormat()
            binding.trackBar.max = duration
        }

        player!!.observeIsPlaying { isPlaying ->
            if (isPlaying) {
                setPlayerUI(PlayerUI.Plays)
                timerHandler.post(timerRunnable)
            } else {
                setPlayerUI(PlayerUI.Paused)
                timerHandler.removeCallbacks(timerRunnable)
            }
        }

    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        context.bindService(
            Intent(context, MediaPlayerService::class.java).putExtra(
                "media",
                mediaLink
            ),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }


}