package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.search.domain.SharedPreferencesWriteRead

class ThemeInteractor(
    private val themeChanger: ThemeChanger,
    private val sharedPreferencesWriteRead: SharedPreferencesWriteRead,
) {

    fun changeTheme(checked: Boolean){
        themeChanger.changeTheme(checked)
    }

    fun saveTheme(isDark: Boolean){
        sharedPreferencesWriteRead.saveTheme(isDark)
    }

    fun getTheme(): Boolean {
        return sharedPreferencesWriteRead.getTheme()
    }
}