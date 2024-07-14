package ru.exursion.domain.settings

import ru.exursion.data.models.User

interface UserSettings {
    val firstName: String?
    val lastName: String?
    val email: String?
    val token: String?
    val avatarImage: String?

    fun fillAllPrefs(user: User?)
}