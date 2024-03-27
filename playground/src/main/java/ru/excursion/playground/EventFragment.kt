package ru.excursion.playground

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.excursion.playground.databinding.TestFragmentBinding
import ru.excursion.playground.player.ACTION_SET_MEDIA
import ru.excursion.playground.player.MediaPlayerService
import ru.excursion.playground.player.PlayerServiceController

class EventFragment: Fragment() {

    private val binding by lazy { TestFragmentBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.player.name = "Ebat' cho za track"
        PlayerServiceController.setMedia("http://192.168.1.65:3000/public/music/1587296546_trevor-daniel-falling.mp3")

        return binding.root
    }

}