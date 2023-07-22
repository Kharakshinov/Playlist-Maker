package com.practicum.playlistmaker.di.audioplayer

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.audioplayer.TrackMediaPlayerImpl
import com.practicum.playlistmaker.domain.audioplayer.TrackMediaPlayer
import org.koin.dsl.module

val audioPlayerDataModule = module{
    factory<TrackMediaPlayer> {
        TrackMediaPlayerImpl(get())
    }

    factory{
        MediaPlayer()
    }
}