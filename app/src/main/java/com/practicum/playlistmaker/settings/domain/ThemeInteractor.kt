package com.practicum.playlistmaker.settings.domain

import android.content.Context
import com.practicum.playlistmaker.search.domain.ISharedPreferencesWriteRead

class ThemeInteractor(
    private val themeChanger: IThemeChanger,
    private val sharedPreferencesWriteRead: ISharedPreferencesWriteRead,
) {

    fun changeTheme(applicationContext: Context, checked: Boolean){
        themeChanger.changeTheme(applicationContext, checked)
    }

    fun saveTheme(isDark: Boolean){
        sharedPreferencesWriteRead.saveTheme(isDark)
    }

    fun getTheme(): Boolean {
        return sharedPreferencesWriteRead.getTheme()
    }
}