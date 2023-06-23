package com.practicum.playlistmaker.medialibrary.di

import com.practicum.playlistmaker.medialibrary.presentation.FavouriteTracksViewModel
import com.practicum.playlistmaker.medialibrary.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryPresentationModule = module {
    viewModel{
        FavouriteTracksViewModel()
    }

    viewModel{
        PlaylistsViewModel()
    }
}