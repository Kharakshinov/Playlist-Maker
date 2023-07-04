package com.practicum.playlistmaker.app

import android.app.Application
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.audioplayer.di.audioPlayerDataModule
import com.practicum.playlistmaker.audioplayer.di.audioPlayerDomainModule
import com.practicum.playlistmaker.audioplayer.di.audioPlayerPresentationModule
import com.practicum.playlistmaker.medialibrary.di.mediaLibraryPresentationModule
import com.practicum.playlistmaker.search.di.searchDataModule
import com.practicum.playlistmaker.search.di.searchDomainModule
import com.practicum.playlistmaker.search.di.searchPresentationModule
import com.practicum.playlistmaker.settings.di.settingsDataModule
import com.practicum.playlistmaker.settings.di.settingsDomainModule
import com.practicum.playlistmaker.settings.di.settingsPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                audioPlayerDataModule, audioPlayerDomainModule, audioPlayerPresentationModule,
                mediaLibraryPresentationModule,
                searchDataModule, searchDomainModule, searchPresentationModule,
                settingsDataModule, settingsDomainModule, settingsPresentationModule,
                )
        }

        res = resources
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

    companion object {
        const val SHARED_PREFERENCES = "shared_preferences_playlistmaker"
        lateinit var res: Resources
    }
}