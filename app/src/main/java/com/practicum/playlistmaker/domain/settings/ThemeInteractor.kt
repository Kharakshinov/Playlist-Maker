package com.practicum.playlistmaker.domain.settings

import com.practicum.playlistmaker.domain.sharedpreferences.SharedPreferencesWriteRead

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