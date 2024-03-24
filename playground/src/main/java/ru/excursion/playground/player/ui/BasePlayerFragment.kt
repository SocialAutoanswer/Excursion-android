package ru.excursion.playground.player.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.excursion.playground.R
import ru.excursion.playground.databinding.AudioPlayerBinding
import ru.excursion.playground.player.ACTION_PAUSE
import ru.excursion.playground.player.ACTION_PLAY
import ru.excursion.playground.player.ACTION_RESUME
import ru.excursion.playground.player.MediaPlayerService
import ru.excursion.playground.toTimeFormat

sealed class PlayerUI(
    @DrawableRes
    val img: Int,
    val action: String
) {
    data object Plays: PlayerUI(R.drawable.ic_pause, ACTION_PAUSE)
    data object Paused: PlayerUI(R.drawable.img, ACTION_PLAY)
}

class BasePlayerFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[PlayerViewModel::class.java] }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MediaPlayerService.LocalBinder
            viewModel.player = binder.service
            viewModel.serviceBound = true
            prepareObservers()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            viewModel.serviceBound = false
        }
    }

    private val onSeekBarChangeListener = object : SimpleOnSeekBarChangedListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            playerBinding.currentTime.text = seekBar!!.progress.toTimeFormat()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            callService(ACTION_PAUSE)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            viewModel.player!!.setCurrentPosition(seekBar!!.progress * 1000)
            playerBinding.currentTime.text = seekBar.progress.toTimeFormat()
            callService(ACTION_RESUME)
        }
    }

    private val playerBinding by lazy { AudioPlayerBinding.inflate(layoutInflater) }

    private var timerRunnable = object : Runnable {

        override fun run() {
            with(playerBinding) {
                playerBinding.trackBar.progress += 1
                currentTime.text = trackBar.progress.toTimeFormat()
            }

            viewModel.timerHandler.postDelayed(this, 1000)

            if (playerBinding.trackBar.progress == viewModel.duration) {
                //viewModel.player!!.setCurrentPosition(0) does not work! (wtf?)
                playerBinding.trackBar.progress = 0
                playerBinding.currentTime.text = 0.toTimeFormat()
                callService(ACTION_PAUSE)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.bindService(
            Intent(context, MediaPlayerService::class.java)
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

        setPlayerUI(PlayerUI.Paused)
        playerBinding.trackBar.setOnSeekBarChangeListener(onSeekBarChangeListener)

        return playerBinding.root
    }

    private fun callService(act: String){
        if(viewModel.serviceBound) {
            requireContext().startService(
                Intent(
                    requireContext(),
                    MediaPlayerService::class.java
                ).apply {
                    action = act
                })
        } else {
            Toast.makeText(requireContext(), R.string.player_unable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun prepareObservers() {
        viewModel.player!!.subscribeOnDuration(viewLifecycleOwner) { dur ->
            viewModel.duration = dur / 1000
            playerBinding.trackDuration.text = viewModel.duration.toTimeFormat()
            playerBinding.trackBar.max = viewModel.duration
        }

        viewModel.player!!.subscribeOnIsPlaying(viewLifecycleOwner) { isPlaying ->
            if(isPlaying){
                setPlayerUI(PlayerUI.Plays)
                viewModel.timerHandler.post(timerRunnable)
            } else {
                setPlayerUI(PlayerUI.Paused)
                viewModel.timerHandler.removeCallbacks(timerRunnable)
            }
        }

    }


    private fun setPlayerUI(playerUI: PlayerUI){
        with(playerBinding){
            playBtn.setImageResource(playerUI.img)
            playBtn.setOnClickListener{ callService(playerUI.action) }
        }
    }

}