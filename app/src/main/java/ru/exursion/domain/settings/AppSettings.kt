package ru.exursion.domain.settings

import android.content.SharedPreferences
import javax.inject.Inject

interface AppSettings {
    var darkThemeState: Boolean
}

class AppSettingsImpl @Inject constructor(
    private val prefs: SharedPreferences
) : AppSettings {

    companion object {
        private const val DARK_THEME_STATE_KEY = "dark_theme_state"
    }
    override var darkThemeState: Boolean = false
        get() = prefs.getBoolean(DARK_THEME_STATE_KEY, false)
        set(state) {
            prefs.edit().putBoolean(DARK_THEME_STATE_KEY, state).apply()
            field = state
        }
}