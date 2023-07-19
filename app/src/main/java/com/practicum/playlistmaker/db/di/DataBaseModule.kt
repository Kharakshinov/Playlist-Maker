package com.practicum.playlistmaker.db.di

import androidx.room.Room
import com.practicum.playlistmaker.db.domain.FavouriteTracksRepository
import com.practicum.playlistmaker.db.AppDatabase
import com.practicum.playlistmaker.db.data.FavouriteTracksRepositoryImpl
import com.practicum.playlistmaker.db.data.converters.TrackDbConverter
import com.practicum.playlistmaker.db.domain.FavouriteTracksInteractor
import com.practicum.playlistmaker.db.domain.FavouriteTracksInteractorImpl
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
}