package ru.exursion.data.models

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate


data class UserRequestDto(
    @SerializedName("user") val user: UserDto?,
    @SerializedName("token") val token: String?
)

data class UserDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("password") val password: String?,
    @SerializedName("last_login") val lastLogin: String?,
    @SerializedName("is_superuser") val isSuperUser: Boolean?,
    @SerializedName("is_staff") val isStaff: Boolean?,
    @SerializedName("is_active") val isActive: Boolean?,
    @SerializedName("date_joined") val joinedDate: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("email_verified") val emailIsVerified: Boolean?,
    @SerializedName("city") val city: String?,
    @SerializedName("birth_date") val birthDate: String?,
    @SerializedName("groups") val groups: List<Long?>?,
    @SerializedName("user_permissions") val permissions: List<Long?>?
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
    var password: String? = "",
    var emailIsVerified: Boolean? = false
)