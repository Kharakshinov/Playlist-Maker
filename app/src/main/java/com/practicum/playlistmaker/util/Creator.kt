package com.practicum.playlistmaker.util

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.data.SearchRepository
import com.practicum.playlistmaker.search.data.SharedPreferencesWriteRead
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.iTunesApi
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

}

