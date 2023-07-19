package com.practicum.playlistmaker.medialibrary.di

import com.practicum.playlistmaker.medialibrary.presentation.favouriteTracks.FavouriteTracksViewModel
import com.practicum.playlistmaker.medialibrary.presentation.playlists.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryPresentationModule = module {
    viewModel{
        FavouriteTracksViewModel(get(), get())
    }

    viewModel{
        PlaylistsViewModel()
    }
}