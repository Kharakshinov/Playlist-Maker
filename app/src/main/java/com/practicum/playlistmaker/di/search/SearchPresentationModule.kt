package com.practicum.playlistmaker.di.search

import com.practicum.playlistmaker.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchPresentationModule = module {
    viewModel{
        SearchViewModel(get())
    }
}