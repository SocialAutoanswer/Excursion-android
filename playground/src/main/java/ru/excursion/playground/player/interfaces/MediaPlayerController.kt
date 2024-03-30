package ru.excursion.playground.player.interfaces

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

interface MediaPlayerController {

    var currentPosition: Int

    fun playMedia()

    fun stopMedia()

    fun pauseMedia()

    fun setVolume(leftVolume: Float, rightVolume: Float)

    fun release()

    fun setMedia(mediaLink: String)

    fun updateDuration(duration: Int) = durationSubject.onNext(duration)
    fun updateIsPlaying(isPlaying: Boolean) = isPlayingSubject.onNext(isPlaying)

    companion object {
        private val durationSubject = BehaviorSubject.create<Int>()
        private val isPlayingSubject = BehaviorSubject.create<Boolean>()

        fun observeDuration(): Observable<Int> = durationSubject.hide()
            .observeOn(AndroidSchedulers.mainThread())

        fun observeIsPlaying(): Observable<Boolean> = isPlayingSubject.hide()
            .observeOn(AndroidSchedulers.mainThread())
    }

}