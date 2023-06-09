package com.practicum.playlistmaker.settings.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.ThemeInteractor

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

    fun setTheme(applicationContext: Context, checked: Boolean){
        themeInteractor.changeTheme(applicationContext, checked)
    }

    fun saveTheme(isDark: Boolean){
        themeInteractor.saveTheme(isDark)
    }

    private fun getTheme(): Boolean {
        return themeInteractor.getTheme()
    }

}