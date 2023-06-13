package com.practicum.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.audioplayer.di.audioPlayerDataModule
import com.practicum.playlistmaker.audioplayer.di.audioPlayerDomainModule
import com.practicum.playlistmaker.audioplayer.di.audioPlayerPresentationModule
import com.practicum.playlistmaker.mainscreen.di.mainScreenPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                audioPlayerDataModule, audioPlayerDomainModule, audioPlayerPresentationModule,
                mainScreenPresentationModule,
                )
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}