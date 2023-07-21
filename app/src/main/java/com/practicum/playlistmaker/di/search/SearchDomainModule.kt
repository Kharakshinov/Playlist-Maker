package com.practicum.playlistmaker.di.search

import com.practicum.playlistmaker.domain.search.SearchInteractor
import org.koin.dsl.module

val searchDomainModule = module{
    factory{
        SearchInteractor(get(), get())
    }
}