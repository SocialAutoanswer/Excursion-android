package ru.exursion.data.auth

import org.threeten.bp.LocalDate
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.User
import ru.exursion.data.models.UserDto
import ru.exursion.data.models.UserRequestDto
import javax.inject.Inject

class UserMapper @Inject constructor(): Mapper<UserRequestDto, User> {
    override fun map(input: UserRequestDto): User = User(
        input.user?.firstName ?: "",
        input.user?.lastName ?: "",
        LocalDate.of(2003, 1, 1), //TODO:not getting it from server
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