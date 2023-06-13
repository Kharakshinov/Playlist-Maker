package com.practicum.playlistmaker.audioplayer.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.audioplayer.data.TrackMediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInterface
import org.koin.dsl.module

val audioPlayerDataModule = module{
    factory<TrackMediaPlayerInterface> {
        TrackMediaPlayer(get())
    }

    factory{
        MediaPlayer()
    }
}