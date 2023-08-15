package com.practicum.playlistmaker.di.db

import androidx.room.Room
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.FavouriteTracksRepositoryImpl
import com.practicum.playlistmaker.data.db.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.data.db.converters.TrackDbConverter
import com.practicum.playlistmaker.domain.db.FavouriteTracksInteractor
import com.practicum.playlistmaker.domain.db.FavouriteTracksInteractorImpl
import com.practicum.playlistmaker.domain.db.FavouriteTracksRepository
import com.practicum.playlistmaker.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.domain.db.PlaylistsInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataBaseModule = module{
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { TrackDbConverter() }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(get(), get())
    }

    single<FavouriteTracksInteractor>{
        FavouriteTracksInteractorImpl(get())
    }

    factory { PlaylistDbConverter() }

    single<PlaylistsRepository>{
        PlaylistsRepositoryImpl(get(), get())
    }

    single<PlaylistsInteractor>{
        PlaylistsInteractorImpl(get())
    }
}