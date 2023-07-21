package com.practicum.playlistmaker.di.search

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.util.app.App
import com.practicum.playlistmaker.data.search.network.ITunesApi
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.SearchRepositoryImpl
import com.practicum.playlistmaker.data.sharedpreferences.SharedPreferencesWriteReadImpl
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.domain.sharedpreferences.SharedPreferencesWriteRead
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {
    factory<SearchRepository>{
        SearchRepositoryImpl(get())
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

    factory<SharedPreferencesWriteRead>{
        SharedPreferencesWriteReadImpl(get())
    }

    factory{
        androidContext().getSharedPreferences(
            App.SHARED_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
    }
}