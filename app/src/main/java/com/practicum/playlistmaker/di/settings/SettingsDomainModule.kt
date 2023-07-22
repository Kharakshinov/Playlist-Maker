package com.practicum.playlistmaker.di.settings

import com.practicum.playlistmaker.domain.settings.SharingInteractor
import com.practicum.playlistmaker.domain.settings.ThemeInteractor
import org.koin.dsl.module

val settingsDomainModule = module {
    factory{
        SharingInteractor(get())
    }

    factory{
        ThemeInteractor(get(), get())
    }
}