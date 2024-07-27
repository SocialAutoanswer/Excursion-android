package ru.exursion.domain.player

import android.app.Service
import android.content.Intent
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import ru.exursion.domain.ext.inject
import ru.exursion.domain.player.impl.AudioFocusControllerImpl
import ru.exursion.domain.player.impl.MediaPlayerControllerImpl
import ru.exursion.domain.player.interfaces.AudioFocusController
import ru.exursion.domain.player.interfaces.MediaPlayerController
import javax.inject.Inject


class MediaPlayerService : Service() {


    @Inject lateinit var mediaPlayer: MediaPlayer
    @Inject lateinit var audioFocusRequestBuilder: AudioFocusRequest.Builder

    private val iBinder: IBinder = LocalBinder()

    private val mediaPlayerController: MediaPlayerController by lazy {
        MediaPlayerControllerImpl(mediaPlayer)
    }

    private val audioFocusController: AudioFocusController by lazy {
        AudioFocusControllerImpl(
            audioFocusRequestBuilder,
            mediaPlayerController,
            this.getSystemService(AUDIO_SERVICE) as AudioManager
        )
    }

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    override fun onBind(intent: Intent): IBinder = iBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!audioFocusController.requestAudioFocus()) {
            stopSelf()
        }

        val setPosition = {
            intent?.getIntExtra("position", 0)?.let { position ->
                mediaPlayerController.setPosition(position)
            }
        }

        val setMedia = {
            intent?.getStringExtra("mediaLink")?.let { mediaLink ->
                mediaPlayerController.setMedia(mediaLink)
            } ?: run {
                Toast.makeText(baseContext, "Медиа недоступна", Toast.LENGTH_LONG).show()
            }
        }

        val actionMap = mapOf(
            ACTION_PLAY to mediaPlayerController::playMedia,
            ACTION_PAUSE to mediaPlayerController::pauseMedia,
            ACTION_STOP to mediaPlayerController::stopMedia,
            ACTION_SET_POSITION to setPosition,
            ACTION_SET_MEDIA to setMedia
        )

        actionMap[intent?.action]?.invoke()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayerController.stopMedia()
        audioFocusController.removeAudioFocus()
    }
    
    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

}