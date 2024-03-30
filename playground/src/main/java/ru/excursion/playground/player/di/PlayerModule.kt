package ru.excursion.playground.player.di

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import dagger.Module
import dagger.Provides
import ru.excursion.playground.player.impl.PlayerManagerImpl
import ru.excursion.playground.player.interfaces.PlayerManager

@Module
class PlayerModule(private val context: Context) {


    @Provides
    fun provideAudioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

    @Provides
    fun provideMediaPlayer(audioAttributes: AudioAttributes) = MediaPlayer().apply {
        setAudioAttributes(audioAttributes)
    }


    @Provides
    fun provideAudioFocusBuilder(audioAttributes: AudioAttributes) : AudioFocusRequest.Builder =
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(audioAttributes)
            .setAcceptsDelayedFocusGain(true)

    @Provides
    fun providePlayerManager() : PlayerManager = PlayerManagerImpl(context)
}