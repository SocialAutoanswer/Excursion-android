package ru.excursion.playground.player.di

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class PlayerModule(private val mediaLink: String) {


    @Provides
    fun provideAudioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

    @Provides
    fun provideMediaPlayer(audioAttributes: AudioAttributes) = MediaPlayer().apply {
        setAudioAttributes(audioAttributes)
        reset()
        setDataSource(mediaLink)
        prepareAsync()
    }


    @Provides
    fun provideAudioFocusBuilder(audioAttributes: AudioAttributes) : AudioFocusRequest.Builder =
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(audioAttributes)
            .setAcceptsDelayedFocusGain(true)

}