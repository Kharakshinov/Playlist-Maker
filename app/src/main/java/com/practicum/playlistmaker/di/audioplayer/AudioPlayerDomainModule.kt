package com.practicum.playlistmaker.di.audioplayer

import com.practicum.playlistmaker.domain.audioplayer.TrackMediaPlayerInteractor
import org.koin.dsl.module

val audioPlayerDomainModule = module{
    factory{
        TrackMediaPlayerInteractor(get())
    }
}