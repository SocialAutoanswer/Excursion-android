package ru.exursion.domain.player

import android.annotation.SuppressLint
import ru.exursion.domain.player.interfaces.PlayerManager
import ru.exursion.ui.shared.OnPlayerClickListener
import javax.inject.Inject

@SuppressLint("CheckResult")
class RoutePlayer @Inject constructor(
    private val playerManager: PlayerManager
) {

    init {
        playerManager.getIsPlayingObservable().subscribe { isPlaying ->
            isSomeonePlaying = isPlaying
        }
    }

    var currentPlayingTrackId: Long? = null
        private set

    var isSomeonePlaying = false
        private set

    val onPlayerClickListener = object : OnPlayerClickListener {
        override fun onPlayClick() = playerManager.play()

        override fun onPauseClick() = playerManager.pause()

        override fun onSetPosition(position: Int) = playerManager.setPosition(position)
    }

    fun setCurrentTrack(trackUrl: String, trackId: Long) {
        currentPlayingTrackId = trackId
        playerManager.setMedia(trackUrl)
    }

    fun observePlayerTimer() = playerManager.getCurrentPositionObservable()
}