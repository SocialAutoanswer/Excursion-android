package ru.excursion.playground.player

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleOwner
import ru.excursion.playground.player.impl.AudioFocusControllerImpl
import ru.excursion.playground.player.impl.PlayerControllerImpl


class MediaPlayerService : Service() {

    private val iBinder: IBinder = LocalBinder()
    private var playerController: PlayerController? = null
    private val audioFocusController: AudioFocusController by lazy {
        AudioFocusControllerImpl(playerController, this)
    }

    fun subscribeOnDuration(lifecycleOwner: LifecycleOwner, callback: (Int) -> Unit) =
        (playerController as PlayerControllerImpl).duration.observe(lifecycleOwner, callback)

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (!audioFocusController.requestAudioFocus()) {
            stopSelf()
        }

        val actionMap = mapOf(
            ACTION_PLAY to playerController!!::playMedia,
            ACTION_PAUSE to playerController!!::pauseMedia,
            ACTION_RESUME to playerController!!::resumeMedia,
            ACTION_STOP to playerController!!::stopMedia
        )

        actionMap[intent.action]?.invoke()

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder {
        playerController = intent?.getStringExtra("media")?.let { mediaLink ->
            PlayerControllerImpl(mediaLink, ::stopSelf)
        }
        return iBinder
    }

    override fun onDestroy() {
        super.onDestroy()

        playerController?.stopMedia()
        audioFocusController.removeAudioFocus()
    }

    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

}