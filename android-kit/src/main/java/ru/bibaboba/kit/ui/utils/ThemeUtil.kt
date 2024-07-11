package ru.bibaboba.kit.ui.utils

import androidx.appcompat.app.AppCompatDelegate

object ThemeUtil {
    fun setDarkThemeState(state: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if(state) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}