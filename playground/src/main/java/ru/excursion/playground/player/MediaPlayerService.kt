package ru.excursion.playground.player

import android.content.Context
import android.content.Intent
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import ru.excursion.playground.player.di.DaggerPlayerComponent
import ru.excursion.playground.player.di.PlayerModule
import ru.excursion.playground.player.impl.AudioFocusControllerImpl
import ru.excursion.playground.player.impl.PlayerControllerImpl
import javax.inject.Inject


class MediaPlayerService : LifecycleService() {


    @Inject
    lateinit var mediaPlayer: MediaPlayer
    @Inject
    lateinit var audioFocusRequestBuilder: AudioFocusRequest.Builder

    private val iBinder: IBinder = LocalBinder()

    private val playerController: PlayerController by lazy {
        PlayerControllerImpl(
            mediaPlayer,
            ::stopSelf
        )
    }

    private val audioFocusController: AudioFocusController by lazy {
        AudioFocusControllerImpl(
            audioFocusRequestBuilder,
            playerController,
            this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        )
    }

    fun observeDuration(callback: (Int) -> Unit) =
        playerController.observeDuration(this, callback)

    fun observeIsPlaying(callback: (Boolean) -> Unit) =
        playerController.observeIsPlaying(this, callback)


    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        intent.getStringExtra("media")?.let { mediaLink ->
            DaggerPlayerComponent.builder()
                .playerModule(PlayerModule(mediaLink))
                .build()
                .inject(this)
        }

        return iBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!audioFocusController.requestAudioFocus()) {
            stopSelf()
        }

        val setPosition = {
            intent?.getIntExtra("position", 0).let { position ->
                playerController.setCurrentPosition(position ?: 0)
            }
        }

        val actionMap = mapOf(
            ACTION_PLAY to playerController::playMedia,
            ACTION_PAUSE to playerController::pauseMedia,
            ACTION_RESUME to playerController::resumeMedia,
            ACTION_STOP to playerController::stopMedia,
            ACTION_SET_POSITION to setPosition
        )

        actionMap[intent?.action]?.invoke()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        playerController.stopMedia()
        audioFocusController.removeAudioFocus()
    }

    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

}