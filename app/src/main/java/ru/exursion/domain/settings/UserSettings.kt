package ru.exursion.domain.settings

interface UserSettings {
    val firstName: String
    val lastName: String
    val email: String
    val token: String
    val avatarImage: String
}