package com.practicum.playlistmaker.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.SharingInteractor
import com.practicum.playlistmaker.domain.settings.ThemeInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: ThemeInteractor
): ViewModel() {
    private val _state = MutableLiveData<SettingsState>()
    val state: LiveData<SettingsState> = _state

    fun setStartState(){
        _state.postValue(SettingsState.Start(getTheme()))
    }

    fun shareApplicationClicked(){
        _state.postValue(SettingsState.ShareApplication)
    }

    fun writeToSupportClicked(){
        _state.postValue(SettingsState.WriteToSupport)
    }

    fun openUserAgreementClicked(){
        _state.postValue(SettingsState.OpenUserAgreement)
    }

    fun switchThemeClicked(checked: Boolean){
        _state.postValue(SettingsState.DarkTheme(checked))
    }

    fun shareApplication(){
        sharingInteractor.shareApplication()
    }

    fun writeToSupport(){
        sharingInteractor.writeToSupport()
    }

    fun openUserAgreement(){
        sharingInteractor.openUserAgreement()
    }

    fun setTheme(checked: Boolean){
        themeInteractor.changeTheme(checked)
    }

    fun saveTheme(isDark: Boolean){
        themeInteractor.saveTheme(isDark)
    }

    private fun getTheme(): Boolean {
        return themeInteractor.getTheme()
    }

}