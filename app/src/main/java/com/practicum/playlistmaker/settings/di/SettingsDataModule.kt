package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.data.ExternalNavigator
import com.practicum.playlistmaker.settings.data.ThemeChanger
import com.practicum.playlistmaker.settings.domain.IExternalNavigator
import com.practicum.playlistmaker.settings.domain.IThemeChanger
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsDataModule = module {
    factory<IExternalNavigator>{
        ExternalNavigator(androidContext())
    }

    factory<IThemeChanger>{
        ThemeChanger(androidContext())
    }
}