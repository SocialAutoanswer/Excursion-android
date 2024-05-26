package ru.exursion.data.models

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val token: String,
    val avatarImage: String,
)