package com.practicum.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.audioplayer.data.TrackMediaPlayer
import com.practicum.playlistmaker.audioplayer.domain.TrackMediaPlayerInteractor
import com.practicum.playlistmaker.audioplayer.presentation.AudioPlayerViewModelFactory
import com.practicum.playlistmaker.search.data.SearchRepository
import com.practicum.playlistmaker.search.data.SharedPreferencesWriteRead
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.iTunesApi
import com.practicum.playlistmaker.settings.data.ExternalNavigator
import com.practicum.playlistmaker.settings.data.ThemeChanger
import com.practicum.playlistmaker.settings.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.ThemeInteractor
import com.practicum.playlistmaker.settings.presentation.SettingsViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private fun provideApi(): iTunesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(iTunesApi::class.java)
    }

    private fun provideRepository():SearchRepository {
        return SearchRepository(provideApi())
    }

    private fun provideSharedPreferencesWriteRead(sharedPreferences: SharedPreferences): SharedPreferencesWriteRead{
        return SharedPreferencesWriteRead(sharedPreferences)
    }

    fun provideSearchInteracor(sharedPreferences: SharedPreferences): SearchInteractor{
        return SearchInteractor(provideSharedPreferencesWriteRead(sharedPreferences), provideRepository())
    }







    private fun provideTrackMediaPlayer(): TrackMediaPlayer{
        return TrackMediaPlayer()
    }

    private fun provideTrackMediaPlayerInteractor(): TrackMediaPlayerInteractor{
        return TrackMediaPlayerInteractor(provideTrackMediaPlayer())
    }

    fun provideAudioPlayerViewModelFactory(url: String): AudioPlayerViewModelFactory{
        return AudioPlayerViewModelFactory(provideTrackMediaPlayerInteractor(), url)
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

