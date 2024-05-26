package ru.exursion.domain.settings

class SettingsImpl(
    private val userSettings: UserSettings
) : Settings {

    override val user: UserSettings
        get() = userSettings

    override val token: String
        get() = user.token
}