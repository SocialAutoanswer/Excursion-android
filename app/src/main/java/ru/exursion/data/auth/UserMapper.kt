package ru.exursion.data.auth

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.BirthDate
import ru.exursion.data.models.User
import ru.exursion.data.models.UserDto
import ru.exursion.data.models.UserRequestDto
import javax.inject.Inject

class UserMapper @Inject constructor(): Mapper<UserRequestDto, User> {
    override fun map(input: UserRequestDto): User = User(
        input.user?.firstName ?: "",
        input.user?.lastName ?: "",
        BirthDate(1, 1, 2003), //TODO:not getting it from server
        input.user?.email ?: "",
        input.token ?: "",
        "", //TODO:not getting it from server
        input.user?.password ?: ""
    )
}

fun User.toDto() = UserDto(
    firstName,
    lastName,
    email,
    password
)