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

    var isSomeonePlaying = false
        private set

    val doIPlay: Boolean
        get() = (currentTrack == trackCandidate && !currentTrack.isNullOrBlank())

    val onPlayerClickListener = object : OnPlayerClickListener {
        override fun onPlayClick() {
            if (!doIPlay || trackCandidate.isNullOrBlank()) setCurrentTrack(trackCandidate)
            else playerManager.play()
        }

        override fun onPauseClick() = playerManager.pause()

        override fun onSetPosition(position: Int) = playerManager.setPosition(position)
    }

    private var currentTrack: String? = null
    private var trackCandidate: String? = null

    fun setTrackCandidate(mediaUrl: String) {
        trackCandidate = mediaUrl
    }

    private fun setCurrentTrack(trackUrl: String?) = trackUrl?.let {
        currentTrack = it
        playerManager.setMedia(it)
    }

    fun observePlayerTimer() = playerManager.getCurrentPositionObservable()
}