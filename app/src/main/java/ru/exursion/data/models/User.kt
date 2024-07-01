package ru.exursion.data.models

import com.google.gson.annotations.SerializedName


data class UserRequestDto(
    @SerializedName("user") val user: UserDto?,
    @SerializedName("token") val token: String?
)

data class UserDto(
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("email") val email: String?
)

data class BirthDate(
    private val day: Int,
    private val month: Int,
    private val year: Int
) {
    val date = "$day-$month-$year"
}

data class User(
    val firstName: String,
    val lastName: String,
    val birthDate: BirthDate,
    val email: String,
    val token: String,
    val avatarImage: String,
)