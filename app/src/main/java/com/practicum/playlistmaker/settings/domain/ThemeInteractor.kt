package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.search.domain.ISharedPreferencesWriteRead

class ThemeInteractor(
    private val themeChanger: IThemeChanger,
    private val sharedPreferencesWriteRead: ISharedPreferencesWriteRead,
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