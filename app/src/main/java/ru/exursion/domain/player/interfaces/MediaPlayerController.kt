package ru.exursion.domain.player.interfaces

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

interface MediaPlayerController {

    fun playMedia()

    fun stopMedia()

    fun pauseMedia()

    fun setVolume(leftVolume: Float, rightVolume: Float)

    fun release()

    fun setMedia(mediaLink: String)

    fun setPosition(position: Int)

    fun updateIsPlaying(isPlaying: Boolean) = isPlayingSubject.onNext(isPlaying)

    fun updateCurrentPosition(currentPosition: Int) = currentPositionSubject.onNext(currentPosition)

    companion object {
        private val isPlayingSubject = BehaviorSubject.create<Boolean>()
        private val currentPositionSubject = BehaviorSubject.create<Int>()

        fun observeIsPlaying(): Observable<Boolean> = isPlayingSubject.hide()
            .observeOn(AndroidSchedulers.mainThread())

        fun observeCurrentPosition(): Observable<Int> = currentPositionSubject.hide()
            .observeOn(AndroidSchedulers.mainThread())
    }

}