package ru.excursion.playground.player.di

import dagger.Component
import ru.excursion.playground.player.MediaPlayerService
import javax.inject.Singleton

@Singleton
@Component(modules = [PlayerModule::class])
interface PlayerComponent {

    fun inject(service: MediaPlayerService)

    @Component.Builder
    interface Builder {
        fun build(): PlayerComponent
        fun playerModule(playerModule: PlayerModule): Builder
    }

}