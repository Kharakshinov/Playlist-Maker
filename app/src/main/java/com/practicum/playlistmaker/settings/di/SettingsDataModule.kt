package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.settings.data.ThemeChangerImpl
import com.practicum.playlistmaker.settings.domain.ExternalNavigator
import com.practicum.playlistmaker.settings.domain.ThemeChanger
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