package com.practicum.playlistmaker.settings.domain

import android.content.Context

interface IThemeChanger {

    fun changeTheme(applicationContext: Context, checked: Boolean)
}