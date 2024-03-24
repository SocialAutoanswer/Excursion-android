package ru.excursion.playground

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.excursion.playground.databinding.AudioPlayerBinding
import ru.excursion.playground.databinding.TestFragmentBinding
import ru.excursion.playground.player.ui.BasePlayerFragment

class EventFragment: BasePlayerFragment() {

    private val binding by lazy { TestFragmentBinding.inflate(layoutInflater) }
    override val playerBinding: AudioPlayerBinding by lazy { AudioPlayerBinding.bind(binding.root) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return binding.root
    }

}