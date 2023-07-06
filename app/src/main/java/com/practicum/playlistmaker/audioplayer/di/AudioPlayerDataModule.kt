package com.practicum.playlistmaker.audioplayer.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.data.TrackMediaPlayerImpl
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayer
import org.koin.dsl.module

val audioPlayerDataModule = module{
    factory<TrackMediaPlayer> {
        TrackMediaPlayerImpl(get())
    }

    factory{
        MediaPlayer()
    }
}