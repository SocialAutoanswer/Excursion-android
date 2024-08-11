package ru.exursion.domain.settings

import android.content.SharedPreferences
import ru.exursion.data.models.User
import ru.exursion.BuildConfig
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

    override var firstName: String?
        get() = prefs.getString(FIRST_NAME_PREFS_KEY, null)
        set(name) = prefs.edit().putString(FIRST_NAME_PREFS_KEY, name).apply()

    override var lastName: String?
        get() = prefs.getString(LAST_NAME_PREFS_KEY, null)
        set(name) = prefs.edit().putString(LAST_NAME_PREFS_KEY, name).apply()

    override var email: String?
        get() = prefs.getString(EMAIL_PREFS_KEY, null)
        set(email) = prefs.edit().putString(EMAIL_PREFS_KEY, email).apply()

    override var token: String?
        get() = prefs.getString(TOKEN_PREFS_KEY, BuildConfig.AUTH_TOKEN)
        set(token) = prefs.edit().putString(TOKEN_PREFS_KEY, token).apply()

    override var avatarImage: String?
        get() = prefs.getString(AVATAR_IMAGE_PREFS_KEY, null)
        set(name) = prefs.edit().putString(AVATAR_IMAGE_PREFS_KEY, name).apply()

    override fun clearAllPrefs() = prefs.edit().clear().apply()

    override fun fillAllUserPrefs(user: User?) {
        firstName = user?.firstName
        lastName = user?.lastName
        email = user?.email
        token = user?.token
        avatarImage = user?.avatarImage
    }

    override fun getUser() = User(firstName, lastName, null, email, token, avatarImage, null, token != null)
}