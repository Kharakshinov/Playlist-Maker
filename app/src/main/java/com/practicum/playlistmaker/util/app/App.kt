package com.practicum.playlistmaker.util.app

import android.app.Application
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.audioplayer.audioPlayerDataModule
import com.practicum.playlistmaker.di.audioplayer.audioPlayerDomainModule
import com.practicum.playlistmaker.di.audioplayer.audioPlayerPresentationModule
import com.practicum.playlistmaker.di.db.dataBaseModule
import com.practicum.playlistmaker.di.medialibrary.mediaLibraryDomainModule
import com.practicum.playlistmaker.di.medialibrary.mediaLibraryPresentationModule
import com.practicum.playlistmaker.di.search.searchDataModule
import com.practicum.playlistmaker.di.search.searchDomainModule
import com.practicum.playlistmaker.di.search.searchPresentationModule
import com.practicum.playlistmaker.di.settings.settingsDataModule
import com.practicum.playlistmaker.di.settings.settingsDomainModule
import com.practicum.playlistmaker.di.settings.settingsPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                audioPlayerDataModule, audioPlayerDomainModule, audioPlayerPresentationModule,
                dataBaseModule,
                mediaLibraryDomainModule, mediaLibraryPresentationModule,
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