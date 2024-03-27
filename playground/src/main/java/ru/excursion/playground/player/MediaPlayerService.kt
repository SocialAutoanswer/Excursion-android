package ru.excursion.playground.player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.excursion.playground.player.di.DaggerPlayerComponent
import ru.excursion.playground.player.impl.AudioFocusControllerImpl
import ru.excursion.playground.player.impl.MediaPlayerControllerImpl
import ru.excursion.playground.player.interfaces.AudioFocusController
import ru.excursion.playground.player.interfaces.MediaPlayerController
import javax.inject.Inject


class MediaPlayerService : Service() {


    @Inject
    lateinit var mediaPlayer: MediaPlayer
    @Inject
    lateinit var audioFocusRequestBuilder: AudioFocusRequest.Builder

    private val iBinder: IBinder = LocalBinder()

    private val mediaPlayerController: MediaPlayerController by lazy {
        MediaPlayerControllerImpl(
            mediaPlayer,
            ::updateDuration,
            ::updateIsPlaying
        )
    }

    private val audioFocusController: AudioFocusController by lazy {
        AudioFocusControllerImpl(
            audioFocusRequestBuilder,
            mediaPlayerController,
            this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        )
    }

    override fun onCreate() {
        super.onCreate()
        DaggerPlayerComponent.builder()
            .build()
            .inject(this)
    }



    override fun onBind(intent: Intent): IBinder = iBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!audioFocusController.requestAudioFocus()) {
            stopSelf()
        }

        val setPosition = {
            intent?.getIntExtra("position", 0).let { position ->
                mediaPlayerController.currentPosition = position ?: 0
            }
        }

        val setMedia = {
            intent?.getStringExtra("mediaLink").let { mediaLink ->
                mediaPlayerController.setMedia(mediaLink ?: "")
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

    private fun updateDuration(duration: Int) = durationSubject.onNext(duration)
    private fun updateIsPlaying(isPlaying: Boolean) = isPlayingSubject.onNext(isPlaying)

    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

    companion object {
        private val durationSubject = BehaviorSubject.create<Int>()
        private val isPlayingSubject = BehaviorSubject.create<Boolean>()

        fun observeDuration(): Observable<Int> = durationSubject.hide()
            .observeOn(AndroidSchedulers.mainThread())

        fun observeIsPlaying(): Observable<Boolean> = isPlayingSubject.hide()
            .observeOn(AndroidSchedulers.mainThread())


    }

}