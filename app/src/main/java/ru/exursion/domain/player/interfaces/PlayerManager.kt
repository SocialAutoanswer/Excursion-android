package ru.exursion.domain.player.interfaces

import io.reactivex.rxjava3.core.Observable

interface PlayerManager {

    fun play()

    fun pause()

    fun setPosition(position: Int)

    fun setMedia(mediaLink: String)

    fun getIsPlayingObservable() : Observable<Boolean>

    fun getCurrentPositionObservable() : Observable<Int>

}