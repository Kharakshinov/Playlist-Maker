package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import org.koin.dsl.module

val settingsDomainModule = module {
    factory{
        SharingInteractor(get())
    }

    factory{
        ThemeInteractor(get(), get())
    }
}