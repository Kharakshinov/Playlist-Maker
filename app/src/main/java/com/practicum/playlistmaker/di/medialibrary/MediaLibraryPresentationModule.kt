package com.practicum.playlistmaker.di.medialibrary

import com.practicum.playlistmaker.presentation.medialibrary.favouriteTracks.FavouriteTracksViewModel
import com.practicum.playlistmaker.presentation.medialibrary.playlists.PlaylistsViewModel
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