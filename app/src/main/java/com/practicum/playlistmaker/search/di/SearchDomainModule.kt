package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.domain.SearchInteractor
import org.koin.dsl.module

val searchDomainModule = module{
    factory{
        SearchInteractor(get(), get())
    }
}