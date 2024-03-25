package ru.excursion.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.excursion.playground.databinding.TestFragmentBinding

class EventFragment: Fragment() {

    private val binding by lazy { TestFragmentBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.player.mediaLink = "http://192.168.1.65:3000/public/music/1587296546_trevor-daniel-falling.mp3"

        return binding.root
    }

}