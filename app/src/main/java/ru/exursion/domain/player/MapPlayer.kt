package ru.exursion.domain.player

import android.annotation.SuppressLint
import ru.exursion.domain.player.interfaces.PlayerManager
import ru.exursion.ui.shared.OnPlayerClickListener
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("CheckResult")
@Singleton
class MapPlayer @Inject constructor(
    private val playerManager: PlayerManager
) {

    init {
        playerManager.getIsPlayingObservable().subscribe { isPlaying ->
            isSomeonePlaying = isPlaying
        }
    }

    private var currentTrack: String? = null
    private var trackCandidate: String? = null

    var isSomeonePlaying = false
        private set

    fun candidateIsCurrent() = (currentTrack == trackCandidate)

    fun candidateIsPlaying() = (isSomeonePlaying && candidateIsCurrent())

    val onPlayerClickListener = object : OnPlayerClickListener {
        override fun onPlayClick() {
            if (!candidateIsCurrent()) setCurrentTrack(trackCandidate)
            else playerManager.play()
        }

        override fun onPauseClick() = playerManager.pause()

        override fun onSetPosition(position: Int) = playerManager.setPosition(position)
    }


    fun setTrackCandidate(mediaUrl: String?) {
        trackCandidate = mediaUrl
    }

    private fun setCurrentTrack(trackUrl: String?) = trackUrl?.let {
        currentTrack = it
        playerManager.setMedia(it)
    }

    fun observePlayerTimer() = playerManager.getCurrentPositionObservable()
}