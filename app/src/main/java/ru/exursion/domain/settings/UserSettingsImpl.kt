package ru.exursion.domain.settings

import android.content.SharedPreferences
import javax.inject.Inject

class UserSettingsImpl @Inject constructor(
    private val prefs: SharedPreferences
) : UserSettings {

    companion object {
        private const val FIRST_NAME_PREFS_KEY = "user_first_name"
        private const val LAST_NAME_PREFS_KEY = "user_last_name"
        private const val EMAIL_PREFS_KEY = "user_email"
        private const val TOKEN_PREFS_KEY = "user_token"
        private const val AVATAR_IMAGE_PREFS_KEY = "user_avatar_image"
    }

    override val firstName: String?
        get() = prefs.getString(FIRST_NAME_PREFS_KEY, null)
    override val lastName: String?
        get() = prefs.getString(LAST_NAME_PREFS_KEY, null)
    override val email: String?
        get() = prefs.getString(EMAIL_PREFS_KEY, null)
    override val token: String?
        get() = prefs.getString(TOKEN_PREFS_KEY, null)
    override val avatarImage: String?
        get() = prefs.getString(AVATAR_IMAGE_PREFS_KEY, null)
}