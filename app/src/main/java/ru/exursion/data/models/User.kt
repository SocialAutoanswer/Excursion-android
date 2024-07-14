package ru.exursion.data.models

import com.google.gson.annotations.SerializedName


data class UserRequestDto(
    @SerializedName("user") val user: UserDto?,
    @SerializedName("token") val token: String?
)

data class UserDto(
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)

data class BirthDate(
    private val day: Int,
    private val month: Int,
    private val year: Int
) {
    val date = "$day-$month-$year"
}

data class User(
    var firstName: String,
    var lastName: String,
    var birthDate: BirthDate,
    var email: String,
    var token: String,
    var avatarImage: String,
    var password: String?
)