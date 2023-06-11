package com.practicum.playlistmaker.settings.data

import android.content.Context
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.settings.domain.IThemeChanger

class ThemeChanger(private val applicationContext: Context): IThemeChanger {

    override fun changeTheme(checked: Boolean){
        (applicationContext as App).switchTheme(checked)
    }
}