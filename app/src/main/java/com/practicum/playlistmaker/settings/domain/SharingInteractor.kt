package com.practicum.playlistmaker.settings.domain

class SharingInteractor(private val navigator: IExternalNavigator) {

    fun shareApplication(){
        navigator.shareApplication()
    }

    fun writeToSupport(){
        navigator.writeToSupport()
    }

    fun openUserAgreement(){
        navigator.openUserAgreement()
    }
}