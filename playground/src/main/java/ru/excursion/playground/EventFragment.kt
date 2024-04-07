package ru.excursion.playground

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.excursion.playground.databinding.TestFragmentBinding
import ru.excursion.playground.player.impl.PlayerManagerImpl
import ru.excursion.playground.player.di.DaggerPlayerComponent
import ru.excursion.playground.player.di.PlayerModule
import ru.excursion.playground.player.interfaces.OnPlayerClickListener
import ru.excursion.playground.player.interfaces.PlayerManager
import javax.inject.Inject

class EventFragment: Fragment() {

    private val binding by lazy { TestFragmentBinding.inflate(layoutInflater) }

    @Inject lateinit var playerManager: PlayerManager

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerPlayerComponent.builder()
            .playerModule(PlayerModule(context))
            .build()
            .inject(this)
    }

    private val onPlayerClickListener = object : OnPlayerClickListener {
        override fun onPlayClick() = playerManager.play()

        override fun onPauseClick() = playerManager.pause()

        override fun onSetPosition(position: Int) = playerManager.setPosition(position)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)


        binding.player.placeName = "Ebat"
        binding.player.setOnPlayerClickListener(onPlayerClickListener)
        binding.player.setDurationObservable(playerManager.getDurationObservable())
        binding.player.setIsPlayingObservable(playerManager.getIsPlayingObservable())
        playerManager.setMedia("http://192.168.1.65:3000/public/music/1587296546_trevor-daniel-falling.mp3")

        return binding.root
    }

}