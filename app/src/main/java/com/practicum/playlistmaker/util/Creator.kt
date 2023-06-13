package com.practicum.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.search.data.SharedPreferencesWriteRead
import com.practicum.playlistmaker.settings.data.ExternalNavigator
import com.practicum.playlistmaker.settings.data.ThemeChanger
import com.practicum.playlistmaker.settings.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import com.practicum.playlistmaker.settings.presentation.SettingsViewModelFactory

object Creator {

    private fun provideSharedPreferencesWriteRead(sharedPreferences: SharedPreferences): SharedPreferencesWriteRead{
        return SharedPreferencesWriteRead(sharedPreferences)
    }

    private fun provideExternalNavigator(context: Context): ExternalNavigator{
        return ExternalNavigator(context)
    }

    private fun provideSharingInteractor(context: Context): SharingInteractor{
        return SharingInteractor(provideExternalNavigator(context))
    }

    private fun providethemeChanger(applicationContext: Context): ThemeChanger{
        return ThemeChanger(applicationContext)
    }

    private fun provideThemeInteractor(sharedPreferences: SharedPreferences, applicationContext: Context): ThemeInteractor{
        return ThemeInteractor(providethemeChanger(applicationContext), provideSharedPreferencesWriteRead(sharedPreferences))
    }

    fun provideSettingsViewModelFactory(context: Context, sharedPreferences: SharedPreferences, applicationContext: Context): SettingsViewModelFactory{
        return SettingsViewModelFactory(provideSharingInteractor(context), provideThemeInteractor(sharedPreferences, applicationContext))
    }
}

