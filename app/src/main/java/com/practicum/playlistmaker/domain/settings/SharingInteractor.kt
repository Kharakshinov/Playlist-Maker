package com.practicum.playlistmaker.domain.settings

class SharingInteractor(private val navigator: ExternalNavigator) {

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