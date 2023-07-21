package com.practicum.playlistmaker.data.settings

import android.content.Context
import com.practicum.playlistmaker.util.app.App
import com.practicum.playlistmaker.domain.settings.ThemeChanger

class ThemeChangerImpl(private val applicationContext: Context): ThemeChanger {

    override fun changeTheme(checked: Boolean){
        (applicationContext as App).switchTheme(checked)
    }
}