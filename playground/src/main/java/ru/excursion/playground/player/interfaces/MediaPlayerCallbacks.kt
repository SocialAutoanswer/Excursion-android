package ru.excursion.playground.player.interfaces

import android.media.MediaPlayer

interface MediaPlayerCallbacks: MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener