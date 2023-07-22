package com.practicum.playlistmaker.di.medialibrary

import com.practicum.playlistmaker.domain.medialibrary.MediaLibraryInteractor
import org.koin.dsl.module

val mediaLibraryDomainModule = module {
    factory{
        MediaLibraryInteractor(get())
    }
}