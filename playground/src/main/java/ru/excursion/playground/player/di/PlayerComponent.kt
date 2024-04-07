package ru.excursion.playground.player.di

import android.content.Context
import dagger.Component
import ru.excursion.playground.EventFragment
import ru.excursion.playground.player.MediaPlayerService
import javax.inject.Singleton

@Singleton
@Component(modules = [PlayerModule::class])
interface PlayerComponent {

    fun inject(service: MediaPlayerService)

    fun inject(fragment: EventFragment)

    @Component.Builder
    interface Builder {
        fun build(): PlayerComponent
        fun playerModule(playerModule: PlayerModule) : Builder
    }

}