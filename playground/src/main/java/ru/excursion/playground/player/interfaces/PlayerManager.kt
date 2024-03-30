package ru.excursion.playground.player.interfaces

import io.reactivex.rxjava3.core.Observable

interface PlayerManager {

    fun play()

    fun pause()

    fun setPosition(position: Int)

    fun setMedia(mediaLink: String)

    fun getDurationObservable() : Observable<Int>

    fun getIsPlayingObservable() : Observable<Boolean>

}