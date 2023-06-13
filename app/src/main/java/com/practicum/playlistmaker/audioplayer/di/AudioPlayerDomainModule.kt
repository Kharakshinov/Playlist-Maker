package com.practicum.playlistmaker.audioplayer.di

import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInteractor
import org.koin.dsl.module

val audioPlayerDomainModule = module{
    factory{
        TrackMediaPlayerInteractor(get())
    }
}