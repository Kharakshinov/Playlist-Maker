package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchPresentationModule = module {
    viewModel{
        SearchViewModel(get())
    }
}