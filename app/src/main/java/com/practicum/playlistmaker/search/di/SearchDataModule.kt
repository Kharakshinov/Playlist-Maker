package com.practicum.playlistmaker.search.di

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.search.data.network.ITunesApi
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.SearchRepository
import com.practicum.playlistmaker.search.data.SharedPreferencesWriteRead
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.ISearchRepository
import com.practicum.playlistmaker.search.domain.ISharedPreferencesWriteRead
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {
    factory<ISearchRepository>{
        SearchRepository(get())
    }

    factory<NetworkClient>{
        RetrofitNetworkClient(get(),androidContext())
    }

    factory<ITunesApi>{
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    factory<ISharedPreferencesWriteRead>{
        SharedPreferencesWriteRead(get())
    }

    factory{
        androidContext().getSharedPreferences(
            App.SHARED_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
    }
}