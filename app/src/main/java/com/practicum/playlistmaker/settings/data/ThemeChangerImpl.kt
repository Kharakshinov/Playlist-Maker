package com.practicum.playlistmaker.settings.data

import android.content.Context
import com.practicum.playlistmaker.util.app.App
import com.practicum.playlistmaker.settings.domain.ThemeChanger

class ThemeChangerImpl(private val applicationContext: Context): ThemeChanger {

    override fun changeTheme(checked: Boolean){
        (applicationContext as App).switchTheme(checked)
    }
}