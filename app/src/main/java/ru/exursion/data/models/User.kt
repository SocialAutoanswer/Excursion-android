package ru.exursion.data.models

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate


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

data class EmailConfirmCode(
    @SerializedName("email_confirm_code") val code: String?
)

data class User(
    var firstName: String? = "",
    var lastName: String? = "",
    var birthDate: LocalDate? = null,
    var email: String? = "",
    var token: String? = "",
    var avatarImage: String? = "",
    var password: String? = ""
)