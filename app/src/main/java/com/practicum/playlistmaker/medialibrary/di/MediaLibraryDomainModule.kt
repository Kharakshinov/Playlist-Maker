package com.practicum.playlistmaker.medialibrary.di

import com.practicum.playlistmaker.medialibrary.domain.MediaLibraryInteractor
import org.koin.dsl.module

val mediaLibraryDomainModule = module {
    factory{
        MediaLibraryInteractor(get())
    }
}