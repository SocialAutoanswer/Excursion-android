package ru.excursion.playground.player.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import ru.excursion.playground.R
import ru.excursion.playground.databinding.AudioPlayerBinding
import ru.excursion.playground.player.ACTION_PAUSE
import ru.excursion.playground.player.ACTION_PLAY
import ru.excursion.playground.player.ACTION_RESUME
import ru.excursion.playground.player.MediaPlayerService
import ru.excursion.playground.toTimeFormat

class BasePlayerFragment : Fragment() {

    private var player: MediaPlayerService? = null
    var serviceBound = false

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MediaPlayerService.LocalBinder
            player = binder.service
            serviceBound = true

            player!!.subscribeOnDuration(viewLifecycleOwner) { dur ->
                duration = dur
                playerBinding.trackDuration.text = duration.toTimeFormat()
                playerBinding.trackBar.max = duration
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }

    private val onSeekBarChangeListener = object : SimpleOnSeekBarChangedListener {
        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

    private val playerBinding by lazy { AudioPlayerBinding.inflate(layoutInflater) }

    private val timerHandler: Handler = Handler(Looper.getMainLooper())
    private var duration = 0

    private var timerRunnable = object : Runnable {

        override fun run() {
            with(playerBinding) {
                playerBinding.trackBar.progress += 1
                currentTime.text = trackBar.progress.toTimeFormat()

                if (trackBar.progress == duration) {
                    trackBar.progress = 0
                    currentTime.text = 0.toTimeFormat()
                }
            }

            timerHandler.postDelayed(this, 1000)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireContext().bindService(
            Intent(requireContext(), MediaPlayerService::class.java)
                .putExtra(
                    "media",
                    "http://192.168.1.65:3000/public/music/1587296546_trevor-daniel-falling.mp3"
                ),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        playerBinding.playBtn.setOnClickListener { startPlayer(0) }
        playerBinding.currentTime.text = 0.toTimeFormat()

        playerBinding.trackBar.setOnSeekBarChangeListener(onSeekBarChangeListener)

        return playerBinding.root
    }


    private fun startPlayer(currentSecond: Int) {

        if (currentSecond == 0) {
            callService(ACTION_PLAY)
        } else {
            callService(ACTION_RESUME)
        }

        with(playerBinding) {
            playBtn.setImageResource(R.drawable.ic_pause)
            playBtn.setOnClickListener { pausePlayer() }
            timerHandler.post(timerRunnable)
        }
    }


    private fun pausePlayer() {
        with(playerBinding) {
            playBtn.setImageResource(R.drawable.img)
            playBtn.setOnClickListener { startPlayer(0) }
            timerHandler.removeCallbacks(timerRunnable)
        }

        callService(ACTION_PAUSE)
    }

    private fun callService(act: String){
        requireContext().startService(
            Intent(
                requireContext(),
                MediaPlayerService::class.java
            ).apply {
                action = act
            })
    }

}