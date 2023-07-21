package com.practicum.playlistmaker.di.settings

import com.practicum.playlistmaker.data.settings.ExternalNavigatorImpl
import com.practicum.playlistmaker.data.settings.ThemeChangerImpl
import com.practicum.playlistmaker.domain.settings.ExternalNavigator
import com.practicum.playlistmaker.domain.settings.ThemeChanger
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsDataModule = module {
    factory<ExternalNavigator>{
        ExternalNavigatorImpl(androidContext())
    }

    factory<ThemeChanger>{
        ThemeChangerImpl(androidContext())
    }
}