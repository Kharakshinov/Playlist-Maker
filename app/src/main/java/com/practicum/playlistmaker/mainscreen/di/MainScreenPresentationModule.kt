package com.practicum.playlistmaker.mainscreen.di

import com.practicum.playlistmaker.mainscreen.presentation.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainScreenPresentationModule = module{
    viewModel{
        MainScreenViewModel()
    }
}