package com.practicum.playlistmaker.settings.data

import android.content.Context
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.settings.domain.IThemeChanger

class ThemeChanger(): IThemeChanger {

    override fun changeTheme(applicationContext: Context, checked: Boolean){
        (applicationContext as App).switchTheme(checked)
    }
}